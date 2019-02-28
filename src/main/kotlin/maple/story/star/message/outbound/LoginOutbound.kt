package maple.story.star.message.outbound

import io.netty.buffer.ByteBuf
import maple.story.star.netty.code.SendCode

data class LoginOutbound(
    val id: String
) : OutboundMapleMessage {

    override val code: SendCode = SendCode.LOGIN_AUTH

    override fun packet(out: ByteBuf): ByteBuf {
        TODO()
    }
}
