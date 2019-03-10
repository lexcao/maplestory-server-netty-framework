package maple.story.star.message.inbound

import io.netty.buffer.ByteBuf
import io.netty.util.internal.StringUtil
import maple.story.star.netty.extension.readAscii

data class LoginInbound(
    override val data: ByteBuf
) : InboundMapleMessage {

    val mac: String
    val username: String
    val password: String

    init {
        mac = getMacAddress(data)
        data.skipBytes(15)
        username = data.readAscii()
        data.skipBytes(Short.SIZE_BYTES)
        password = data.readAscii()
    }

    private fun getMacAddress(data: ByteBuf): String {
        val bytes = IntArray(6)
        for (i in bytes.indices) {
            bytes[i] = data.readByte().toInt()
        }

        return bytes.joinToString("-") {
            StringUtil.byteToHexStringPadded(it).toUpperCase()
        }
    }
}