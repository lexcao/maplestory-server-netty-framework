package maple.story.star.netty.domain

import io.netty.buffer.ByteBuf
import maple.story.star.code.Recv
import maple.story.star.code.Send
import maple.story.star.netty.extension.hex

data class MaplePacket(

    val id: Int,
    val data: ByteBuf
) {

    private val recv: Recv = Recv.of(id)
    private val send: Send = Send.of(id)
    val length: Int = data.readableBytes()

    fun needLogin(): Boolean = recv.auth

    override fun toString(): String {
        return """
            [H: ${id.hex()}]
            [R: $recv]
            [S: $send]
            [D: ${data.hex()}]
        """.trimIndent()
    }
}
