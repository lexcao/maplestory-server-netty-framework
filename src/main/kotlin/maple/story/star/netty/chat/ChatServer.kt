package maple.story.star.netty.chat

import maple.story.star.netty.common.MapleServer
import org.springframework.stereotype.Controller

@Controller
class ChatServer : MapleServer() {

    override val port: Int = 6002
}