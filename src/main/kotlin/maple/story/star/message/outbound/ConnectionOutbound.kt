package maple.story.star.message.outbound

import io.netty.buffer.ByteBuf
import maple.story.star.code.Send
import maple.story.star.netty.extension.end

class ConnectionOutbound : OutboundMapleMessage {

    override val code: Send = Send.CREATING_CONNECTION

    override fun packet(out: ByteBuf) {
        out.writeShortLE(code.id)
        out.end()
    }
}