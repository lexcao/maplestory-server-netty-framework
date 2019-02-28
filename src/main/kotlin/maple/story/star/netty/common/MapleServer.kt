package maple.story.star.netty.common

import io.netty.util.AttributeKey
import mu.KLogging

abstract class MapleServer {

    companion object : KLogging() {
        val SERVER: AttributeKey<MapleServer> = AttributeKey.newInstance("SERVER")
    }

    abstract val port: Int

    private lateinit var netty: NettyConnection

    val name: String = this.javaClass.simpleName

    fun startup() {
        logger.info("[$name] Netty server starting")
        init()
        netty = NettyConnection(port, this)
        logger.info("[$name] Netty server startup at PORT[$port]")
    }

    open fun init() {}

    fun shutdown() {
        logger.info("[$name] Netty server stopping")
        netty.shutdown()
        logger.info("[$name] Netty server shutdown at PORT[$port]")
    }
}