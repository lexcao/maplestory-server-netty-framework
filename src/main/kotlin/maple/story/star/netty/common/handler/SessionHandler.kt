package maple.story.star.netty.common.handler

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import maple.story.star.client.MapleClient
import maple.story.star.controller.SessionController
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

        val session = context.channel()
        val client = MapleClient(session)

        val hello = SessionController.hello(client)
        val buffer = context.alloc().buffer()
        hello.packet(buffer)

        val packet = buffer.compact()
        context.writeAndFlush(packet)

        context.client(client)

        val server = context.server()
        logger.info("[${server.name}] Session : ip[{}]", ip)
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