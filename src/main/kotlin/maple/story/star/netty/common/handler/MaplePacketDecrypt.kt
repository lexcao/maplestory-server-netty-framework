package maple.story.star.netty.common.handler

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.compact
import mu.KLogging

class MaplePacketDecrypt : SimpleChannelInboundHandler<MaplePacket>() {

    companion object : KLogging()

    override fun channelRead0(
        context: ChannelHandlerContext,
        origin: MaplePacket
    ) {
        val client = context.client() ?: return

        val decrypted = client.decrypt(origin.data)

        val packet = decrypted.packet()
        logger.info { packet }

        if (packet.needLogin() && !client.login) return

        context.fireChannelRead(packet)
    }

    private fun ByteBuf.packet(): MaplePacket {
        val id = readShortLE().toInt()
        val data = compact()
        return MaplePacket(
            id = id,
            data = data
        )
    }
}