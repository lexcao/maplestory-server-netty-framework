package maple.story.star.netty.common.handler

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import maple.story.star.message.OutboundMapleMessage
import maple.story.star.netty.action.ActionProcessor
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.compact
import mu.KLogging
import org.springframework.stereotype.Component

@Component
@ChannelHandler.Sharable
class MapleServerHandler(
    private val actionProcessor: ActionProcessor
) : SimpleChannelInboundHandler<MaplePacket>() {

    companion object : KLogging()

    override fun channelRead0(
        context: ChannelHandlerContext,
        packet: MaplePacket
    ) {
        val client = context.client()!!

        // TODO make sure if this is necessary
//        if (client == null || !client.receiving) {
//            return
//        }

        val result = actionProcessor.process(packet, client)

        if (result is OutboundMapleMessage) {
            // TODO handle broadcast
            val out = result.packet(context)
            context.writeAndFlush(out)
        }
    }

    private fun OutboundMapleMessage.packet(
        context: ChannelHandlerContext
    ): ByteBuf {
        // TODO handle buffer size greater than 256
        val buffer = context.alloc().buffer()
        packet(buffer)
        return buffer.compact()
    }
}