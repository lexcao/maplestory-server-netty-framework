package maple.story.star.message

import io.netty.buffer.ByteBuf
import maple.story.star.code.Send

/**
 *  outbound maple message
 *  the classes which implements this interface will
 *  write member fields and code to bytes [ByteBuf]
 */
interface OutboundMapleMessage {

    val code: Send

    fun packet(out: ByteBuf)
}