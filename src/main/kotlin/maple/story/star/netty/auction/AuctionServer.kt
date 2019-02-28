package maple.story.star.netty.auction

import maple.story.star.netty.common.MapleServer
import org.springframework.stereotype.Controller

@Controller
class AuctionServer : MapleServer() {

    override val port: Int = 6004

    override fun init() {
    }
}