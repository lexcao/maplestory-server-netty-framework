package maple.story.star.netty.domain

import io.netty.buffer.ByteBuf
import maple.story.star.netty.code.RecvCode
import maple.story.star.netty.extension.hex
import maple.story.star.netty.extension.print

data class MaplePacket(

    val id: Int,
    val data: ByteBuf
) {

    val action: RecvCode = RecvCode.of(id)
    val length: Int = data.readableBytes()

    override fun toString(): String {
        return """
            MaplePacket(
                id = ${id.hex()},
                data :
${data.print()}
                )
        """.trimIndent()
    }
}
