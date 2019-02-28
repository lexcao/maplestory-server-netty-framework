package maple.story.star.netty.channel

import maple.story.star.netty.common.MapleServer
import org.springframework.stereotype.Controller

@Controller
class ChannelServer : MapleServer() {

    override val port: Int = 6005
}