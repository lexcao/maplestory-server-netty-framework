package maple.story.star.message.outbound

import io.netty.buffer.ByteBuf
import maple.story.star.code.Send

data class LoginOutbound(
    val id: String
) : OutboundMapleMessage {

    override val code: Send = Send.LOGIN_AUTH

    override fun packet(out: ByteBuf) {
        TODO()
    }
}
