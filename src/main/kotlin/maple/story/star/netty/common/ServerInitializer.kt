package maple.story.star.netty.common

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.IdleStateHandler
import maple.story.star.netty.MaplePacketDecoder
import maple.story.star.netty.MaplePacketEncoder
import maple.story.star.netty.MapleServerHandler

class ServerInitializer : ChannelInitializer<SocketChannel>() {

    override fun initChannel(channel: SocketChannel) {
        channel.pipeline()
            .addLast("heartbeat", IdleStateHandler(25, 25, 0))
            .addLast("decoder", MaplePacketDecoder())
            .addLast("encoder", MaplePacketEncoder())
            .addLast("handler", MapleServerHandler())
    }
}