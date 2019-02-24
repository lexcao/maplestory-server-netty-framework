package maple.story.star.netty

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import maple.story.star.netty.action.SendCode
import maple.story.star.netty.extension.bytes
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.send

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

        val header = client.AES.generateHeader(packet.length)

        // TODO lock ?
        val encrypted = client.AES.encrypt(packet.data).bytes()

        outbound.writeBytes(header + encrypted)
    }
}
