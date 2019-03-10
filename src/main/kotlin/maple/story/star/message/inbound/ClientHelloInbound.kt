package maple.story.star.message.inbound

import io.netty.buffer.ByteBuf

class ClientHelloInbound(
    override val data: ByteBuf
) : InboundMapleMessage {

    // [14 00] [04] [63 00] 01 00

    val mapleType: Int = data.readByte().toInt()
    val mapleVersion: Int = data.readShortLE().toInt()
    val patchString: String = data.readShortLE().toString()
}