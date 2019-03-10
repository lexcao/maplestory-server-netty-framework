package maple.story.star.message.inbound

import io.netty.buffer.ByteBuf
import maple.story.star.netty.extension.readAscii

data class ClientErrorMessageInbound(
    override val data: ByteBuf
) : InboundMapleMessage {

    val message: String = data.readAscii()
}