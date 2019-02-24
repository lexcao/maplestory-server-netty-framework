package maple.story.star.netty.common

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import mu.KLogging

class NettyConnection(
    private val port: Int,
    private val world: Int = -1,
    private val channels: Int = -1
) {

    private companion object : KLogging()

    private val boss: EventLoopGroup = NioEventLoopGroup()
    private val work: EventLoopGroup = NioEventLoopGroup()
    private lateinit var channel: Channel

    fun run() {
        try {
            val server = ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, ServerConstants.MAX_CONNECTIONS)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(ServerInitializer())

            val future = server.bind(port).sync()
            logger.info("start server at port [{}]", port)
            channel = future.channel()
            channel.closeFuture().sync()
        } finally {
//            close()
        }
    }

    fun close() {
        channel.close()
        boss.shutdownGracefully()
        work.shutdownGracefully()
    }
}