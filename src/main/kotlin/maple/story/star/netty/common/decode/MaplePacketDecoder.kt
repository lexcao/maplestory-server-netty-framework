package maple.story.star.netty.common.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import maple.story.star.code.Recv
import maple.story.star.netty.common.handler.MaplePacketDecrypt
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.compact
import mu.KLogging

class MaplePacketDecoder : ByteToMessageDecoder() {

    companion object : KLogging()

    override fun decode(
        context: ChannelHandlerContext,
        inbound: ByteBuf,
        message: MutableList<Any>
    ) {
        val client = context.client() ?: return

        if (inbound.readableBytes() < 4) {
            return
        }

        val packet = inbound.packet()
        logger.info { packet }

        if (!client.login && isLoginPacket(packet.id)) {
            context.pipeline().remove(MaplePacketDecrypt::class.java)
            context.fireChannelRead(packet)
            return
        }

        if (!client.valid(packet) && !client.receiving) {
            context.channel().disconnect()
            return
        }

        if (packet.data.readableBytes() < 2) {
            return
        }

        message.add(packet)
    }

    private fun isLoginPacket(id: Int) = id == Recv.LOGIN.id

    private fun ByteBuf.packet(): MaplePacket {
        val id = readShortLE().toInt()
        val len = readShortLE().toInt()

        val length: Int = if (isLoginPacket(id)) len
        else id xor len

        val data = compact(length)
        return MaplePacket(
            id = id,
            data = data
        )
    }
}