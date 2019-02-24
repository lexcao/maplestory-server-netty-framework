package maple.story.star.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import maple.story.star.client.MapleClient
import maple.story.star.netty.domain.MapleDecryptPacket
import maple.story.star.netty.extension.clear
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.ip
import maple.story.star.netty.login.LoginPacketHandler
import mu.KLogging
import java.util.concurrent.ConcurrentHashMap

class MapleServerHandler : SimpleChannelInboundHandler<MapleDecryptPacket>() {

    companion object : KLogging()

    // IP 限流跟踪
    private val tracker: MutableMap<String, Pair<Long, Byte>> = ConcurrentHashMap()


    /**
     *  login server IP check
     */
    override fun channelActive(ctx: ChannelHandlerContext) {
        println("call 2")
        // TODO check ip earlier
        val ip = ctx.ip()

        // TODO block ip here
        // if () ctx.get().close() return

        // TODO limit ip
        limit(ip)

        if (isShutDown()) {
            ctx.channel().close()
            return
        }

        val client = MapleClient(
            session = ctx.channel(),
            channelId = 1, // TODO get id
            worldId = 1 // TODO world id
        )

        ctx.channel().writeAndFlush(LoginPacketHandler.hello(client))

        ctx.client(client)

        logger.info("session : ip[{}]", ip)
    }


    override fun channelInactive(ctx: ChannelHandlerContext) {
        val client = ctx.client()
        client?.run {
            try {
                disconnect(true, false, false)
            } catch (e: Exception) {
                logger.error("连接异常关闭", e)
            } finally {
                ctx.clear()
                ctx.channel().close()
            }
        }
    }

    private fun isShutDown(): Boolean {
        return false
    }

    /**
     *  同一个 IP 链接限流
     */
    private fun limit(ip: String) {
        // 过滤本地
        if ("127.0.0.1" == ip) {
            return
        }
    }

    override fun channelRead0(
        context: ChannelHandlerContext,
        packet: MapleDecryptPacket
    ) {

        val client = context.client()
        // TODO handle client.isReceiving
        if (client == null || !client.isReceiving()) return

        if (packet.action.needLoggin && !client.isLoggedIn()) return

        handlePacket(packet, client)
    }

    private fun handlePacket(
        action: MapleDecryptPacket,
        client: MapleClient
    ) {

    }
}