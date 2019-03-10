package maple.story.star.message

import io.netty.buffer.ByteBuf

/**
 *  inbound maple message
 *  the classes which implements this interface will
 *  read bytes from [data] and map them to member fields
 */
interface InboundMapleMessage {

    val data: ByteBuf
}
