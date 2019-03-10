package maple.story.star.message.outbound

import io.netty.buffer.ByteBuf
import maple.story.star.code.Send

interface OutboundMapleMessage {

    val code: Send

    fun packet(out: ByteBuf)
}