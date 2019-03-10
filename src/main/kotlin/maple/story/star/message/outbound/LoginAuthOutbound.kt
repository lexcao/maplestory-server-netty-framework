package maple.story.star.message.outbound

import io.netty.buffer.ByteBuf
import maple.story.star.code.Send
import maple.story.star.netty.extension.writeAscii
import java.text.SimpleDateFormat
import java.util.Date

class LoginAuthOutbound(
    val login: String
) : OutboundMapleMessage {

    override val code: Send = Send.LOGIN_AUTH

    override fun packet(out: ByteBuf) {
        out.writeShortLE(code.id)

        out.writeAscii(login)

        out.writeIntLE(datetime())
    }

    private fun datetime(): Int = SimpleDateFormat("yyyyMMddHH").format(Date()).toInt()
}