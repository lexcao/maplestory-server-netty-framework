package maple.story.star.netty.domain

import io.netty.buffer.ByteBuf
import maple.story.star.netty.extension.hex
import maple.story.star.netty.extension.print

data class MaplePacket(
    val id: Int,
    val data: ByteBuf,
    val length: Int
) {

    override fun toString(): String {
        return """
            MaplePacket(
                id = ${id.hex()},
                length = ${length.hex()}($length),
                data :
${data.print()}
                )
        """.trimIndent()
    }
}
