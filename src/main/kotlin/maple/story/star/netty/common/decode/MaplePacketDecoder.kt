package maple.story.star.netty.common.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.client
import mu.KLogging

class MaplePacketDecoder : ByteToMessageDecoder() {

    companion object : KLogging()

    override fun decode(
        context: ChannelHandlerContext,
        inbound: ByteBuf,
        message: MutableList<Any>
    ) {
        val client = context.client() ?: return

        val decrypted = client.decrypt(inbound)

        val packet = decrypted.packet()

        if (packet.needLogin() && !client.login) return

        message.add(packet)
    }

    private fun ByteBuf.packet(): MaplePacket {
        val id = readShortLE().toInt()
        val data = readSlice(readableBytes())
        return MaplePacket(
            id = id,
            data = data
        )
    }

    private fun MaplePacket.needLogin(): Boolean = action.auth
}