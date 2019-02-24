package maple.story.star.netty.login

import maple.story.star.netty.common.AbstractServer
import maple.story.star.netty.common.NettyConnection
import maple.story.star.netty.login.config.LoginConfig
import org.springframework.stereotype.Component

@Component
class LoginServer(
    val loginConfig: LoginConfig
) : AbstractServer() {

    lateinit var server: NettyConnection

    override fun startup() {
        server = NettyConnection(loginConfig.port)
        server.run()
    }

    override fun shutdown() {
        server.close()
    }
}