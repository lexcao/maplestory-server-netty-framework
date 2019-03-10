package maple.story.star.netty.extension

import io.netty.channel.ChannelHandlerContext
import io.netty.util.AttributeKey
import maple.story.star.client.MapleClient
import maple.story.star.netty.common.MapleServer
import java.net.InetSocketAddress

fun <T> ChannelHandlerContext.get(key: AttributeKey<T>): T? =
    channel().attr(key).get()

fun ChannelHandlerContext.client(): MapleClient? =
    get(MapleClient.CLIENT)

fun ChannelHandlerContext.server(): MapleServer =
    get(MapleServer.SERVER)!!

fun <T> ChannelHandlerContext.set(key: AttributeKey<T>, value: T) {
    channel().attr(key).set(value)
}

fun ChannelHandlerContext.client(client: MapleClient) {
    set(MapleClient.CLIENT, client)
}

fun ChannelHandlerContext.ip(): String =
    (channel().remoteAddress() as InetSocketAddress).hostString

fun ChannelHandlerContext.clear() {
    channel().attr(MapleClient.CLIENT).set(null)
}

fun ChannelHandlerContext.closeClient() {
    clear()
    channel().close()
}