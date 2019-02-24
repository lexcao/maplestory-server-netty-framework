package maple.story.star.netty

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import maple.story.star.netty.extension.client
import maple.story.star.netty.extension.packet
import maple.story.star.netty.extension.print
import maple.story.star.netty.extension.receive
import maple.story.star.netty.login.LoginPacketHandler
import mu.KLogging

class MaplePacketDecoder : ByteToMessageDecoder() {

    companion object : KLogging()

    override fun channelActive(ctx: ChannelHandlerContext) {
        println("call 1")
    }

    /**
     * login packet:
     * 69 69 29 00
     * |			header			 |
     * |  packet id | packet length  |
     * [    69 69   |      29 0      ]
     */
    override fun decode(
        context: ChannelHandlerContext,
        inbound: ByteBuf,
        message: MutableList<Any>
    ) {
        logger.info("inbound: \n{}", inbound.print())
        val client = context.client() ?: return

        if (inbound.readableBytes() < 4) {
            return
        }

        val received = inbound.receive()

        if (!client.isLoggedIn()) {
            LoginPacketHandler.login(received.data, client)
            return
        }

        if (!client.AES.validReceivedPacketId(received.id) && !client.isReceiving()) {
            context.channel().disconnect()
            return
        }

        if (received.data.readableBytes() < 2) return

        // decrypt
        val decrypted = client.AES.decrypt(received.data)

        message.add(decrypted.packet())

        logger.info("receive packet \n{}", received)
    }

}