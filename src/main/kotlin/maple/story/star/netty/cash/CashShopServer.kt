package maple.story.star.netty.cash

import maple.story.star.netty.common.MapleServer
import org.springframework.stereotype.Controller

@Controller
class CashShopServer : MapleServer() {

    override val port: Int = 6003

    override fun init() {
        // TODO player storage
    }
}