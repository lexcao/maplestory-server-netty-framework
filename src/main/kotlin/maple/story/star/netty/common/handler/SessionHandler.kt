package maple.story.star.netty.common.handler

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import maple.story.star.client.MapleClient
import maple.story.star.netty.extension.clear
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.ip
import maple.story.star.netty.extension.server
import maple.story.star.netty.login.LoginPacketHandler
import mu.KLogging

@ChannelHandler.Sharable
class SessionHandler : ChannelInboundHandlerAdapter() {

    companion object : KLogging()

    override fun channelActive(ctx: ChannelHandlerContext) {
        val ip = ctx.ip()

        // TODO block ip here
        // if () ctx.get().close() return

        // TODO limit ip
        limit(ip)

//        if (isShutDown()) {
//            ctx.channel().close()
//            return
//        }

        val session = ctx.channel()
        val client = MapleClient(session)

        session.writeAndFlush(LoginPacketHandler.hello(client))

        ctx.client(client)

        val server = ctx.server()
        logger.info("[${server.name}] Session : ip[{}]", ip)
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

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val client = ctx.client()
        client?.run {
            try {
                disconnect(true, false, false)
            } catch (e: Exception) {
                MapleServerHandler.logger.error("连接异常关闭", e)
            } finally {
                ctx.clear()
                ctx.channel().close()
            }
        }
    }
}