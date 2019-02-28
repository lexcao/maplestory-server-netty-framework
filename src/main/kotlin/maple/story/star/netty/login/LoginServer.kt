package maple.story.star.netty.login

import maple.story.star.netty.common.MapleServer
import org.springframework.stereotype.Controller

@Controller
class LoginServer : MapleServer() {

    override val port: Int = 6001
}