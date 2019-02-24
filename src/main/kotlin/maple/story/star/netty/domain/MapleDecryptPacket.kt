package maple.story.star.netty.domain

import io.netty.buffer.ByteBuf
import maple.story.star.netty.action.RecvCode
import maple.story.star.netty.extension.hex
import maple.story.star.netty.extension.print

data class MapleDecryptPacket(

    val id: Int,
    val action: RecvCode,
    val data: ByteBuf
) {

    override fun toString(): String {
        return """
            MaplePacket(
                id = ${id.hex()},
                action = $action
                data :
${data.print()}
                )
        """.trimIndent()
    }
}
