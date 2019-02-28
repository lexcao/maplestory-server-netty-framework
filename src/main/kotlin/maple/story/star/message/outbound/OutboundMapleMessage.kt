package maple.story.star.message.outbound

import io.netty.buffer.ByteBuf
import maple.story.star.netty.code.SendCode

interface OutboundMapleMessage {

    val code: SendCode

    fun packet(out: ByteBuf): ByteBuf
}