package maple.story.star.netty.common.encode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.bytes
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.compact
import mu.KLogging

class MaplePacketEncoder : MessageToByteEncoder<ByteBuf>() {

    companion object : KLogging()

    override fun encode(
        context: ChannelHandlerContext,
        message: ByteBuf,
        outbound: ByteBuf
    ) {
        val client = context.client()

        if (client == null) {
            // no client object created yet, send unencrypted
            outbound.writeBytes(message.compact())
            return
        }

        val packet = message.send()
        logger.info { packet }

        client.lock {
            val header = client.header(packet.length)
            val encrypted = client.encrypt(packet.data).bytes()

            val result = header + encrypted
            outbound.writeBytes(result)
        }
    }

    private fun ByteBuf.send(): MaplePacket {
        val id = getShortLE(0).toInt()
        val data = compact()
        return MaplePacket(
            id = id,
            data = data
        )
    }
}
