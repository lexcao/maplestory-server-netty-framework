package maple.story.star.netty.common.handler

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import maple.story.star.client.MapleClient
import maple.story.star.handler.HelloHandler
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.closeClient
import maple.story.star.netty.extension.compact
import maple.story.star.netty.extension.ip
import maple.story.star.netty.extension.server
import mu.KLogging

@ChannelHandler.Sharable
class SessionHandler : ChannelInboundHandlerAdapter() {

    companion object : KLogging()

    override fun channelActive(context: ChannelHandlerContext) {
        val ip = context.ip()

        // TODO block ip here
        // if () context.get().close() return

        // TODO limit ip
        limit(ip)

//        if (isShutDown()) {
//            context.channel().close()
//            return
//        }

        val session = context.channel()
        val client = MapleClient(session)

        val hello = HelloHandler.hello(client)
        val buffer = context.alloc().buffer()
        hello.packet(buffer)

        val packet = buffer.compact()
        context.writeAndFlush(packet)

        context.client(client)

        val server = context.server()
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
                logger.error("连接异常关闭", e)
            } finally {
                ctx.closeClient()
            }
        }
    }
}