package maple.story.star.netty.common.decode

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import maple.story.star.netty.code.RecvCode
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.print
import maple.story.star.netty.login.LoginPacketHandler
import mu.KLogging

class LoginPacketDecoder : ByteToMessageDecoder() {

    companion object : KLogging()

    override fun decode(
        context: ChannelHandlerContext,
        inbound: ByteBuf,
        message: MutableList<Any>
    ) {
        logger.info(inbound.print())

        val client = context.client() ?: return

        if (inbound.readableBytes() < 4) {
            return
        }

        val packet = inbound.packet()

        if (!client.login && packet.login()) {
            // TODO handle with @Action
            LoginPacketHandler.login(packet.data, client)
            return
        }

        if (!client.valid(packet) && !client.receiving) {
            context.channel().disconnect()
            return
        }

        if (packet.data.readableBytes() < 2) {
            return
        }

        message.add(packet.data)
    }

    private fun MaplePacket.login(): Boolean = id == RecvCode.LOGIN.id

    private fun ByteBuf.packet(): MaplePacket {
        val id = readShortLE().toInt()
        skipBytes(Short.SIZE_BYTES)
        val data = readSlice(readableBytes())
        return MaplePacket(
            id = id,
            data = data
        )
    }
}