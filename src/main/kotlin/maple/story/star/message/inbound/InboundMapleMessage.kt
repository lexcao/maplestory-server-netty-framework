package maple.story.star.message.inbound

import io.netty.buffer.ByteBuf

interface InboundMapleMessage {

    val data: ByteBuf
}
