package maple.story.star.netty.common

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import maple.story.star.netty.common.initial.ServerInitializer

class NettyConnection(
    port: Int,
    server: MapleServer
) {

    private val boss: EventLoopGroup = NioEventLoopGroup()
    private val work: EventLoopGroup = NioEventLoopGroup()
    private val channel: Channel

    init {
        val boot = ServerBootstrap()
            .group(boss, work)
            .channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 1_000)
            .childAttr(MapleServer.SERVER, server)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(ServerInitializer())

        channel = boot.bind(port).sync().channel()
            .closeFuture().channel()
    }

    fun shutdown() {
        channel.close()
        boss.shutdownGracefully()
        work.shutdownGracefully()
    }
}