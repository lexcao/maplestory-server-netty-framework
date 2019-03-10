package maple.story.star.message.outbound

import io.netty.buffer.ByteBuf
import maple.story.star.client.MapleClient
import maple.story.star.netty.extension.end
import maple.story.star.netty.extension.writeAscii

data class HelloOutbound(
    val length: Int,
    val mapleVersion: Int,
    val patchString: String,
    val client: MapleClient,
    val loginMapleType: Int
) {

    fun packet(out: ByteBuf) {
        out.writeShortLE(length)
            .writeShortLE(mapleVersion)
            .writeAscii(patchString)
            .writeBytes(client.recvIV())
            .writeBytes(client.sendIV())
            .writeShortLE(loginMapleType)

            .end()
    }
}