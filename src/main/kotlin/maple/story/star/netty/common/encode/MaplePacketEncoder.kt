package maple.story.star.netty.common.encode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import maple.story.star.netty.code.SendCode
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.bytes
import maple.story.star.netty.extension.client

class MaplePacketEncoder : MessageToByteEncoder<ByteBuf>() {

    override fun encode(
        context: ChannelHandlerContext,
        message: ByteBuf,
        outbound: ByteBuf
    ) {
        val client = context.client()

        if (client == null) {
            // 还未进入客户端 无需加密
            outbound.writeBytes(message)
            return
        }

        val packet = message.send()

        // TODO get op code and log
        val operation = SendCode.of(packet.id)

        val header = client.header(packet.length)

        // TODO lock ?
        val encrypted = client.encrypt(packet.data).bytes()

        outbound.writeBytes(header + encrypted)
    }

    private fun ByteBuf.send(): MaplePacket {
        val id = readShortLE().toInt()
        val data = readSlice(readableBytes())
        return MaplePacket(
            id = id,
            data = data
        )
    }
}
