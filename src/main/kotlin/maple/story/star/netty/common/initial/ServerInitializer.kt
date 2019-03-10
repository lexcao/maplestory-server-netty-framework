package maple.story.star.netty.common.initial

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.IdleStateHandler
import maple.story.star.bean.BeanFactory
import maple.story.star.netty.common.decode.MaplePacketDecoder
import maple.story.star.netty.common.encode.MaplePacketEncoder
import maple.story.star.netty.common.handler.MaplePacketDecrypt
import maple.story.star.netty.common.handler.MapleServerHandler
import maple.story.star.netty.common.handler.SessionHandler

class ServerInitializer : ChannelInitializer<SocketChannel>() {

    private val sessionHandler: ChannelHandler = SessionHandler()
    private val serverHandler: MapleServerHandler = BeanFactory.get()

    override fun initChannel(channel: SocketChannel) {
        channel.pipeline()
            .addLast("heartbeat", IdleStateHandler(25, 25, 0))
            .addLast("session", sessionHandler)
            .addLast("logger", LoggingHandler(LogLevel.INFO))
            .addLast("encoder", MaplePacketEncoder())
            .addLast("decoder", MaplePacketDecoder())
            .addLast("decrypt", MaplePacketDecrypt())
            .addLast("handler", serverHandler)
    }
}