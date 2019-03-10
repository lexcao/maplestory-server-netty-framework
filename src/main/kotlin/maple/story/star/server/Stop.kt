package maple.story.star.server

import maple.story.star.netty.common.MapleServer
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component


@Component
class Stop(
    val servers: ObjectProvider<MapleServer>
) : Thread("Stop") {

    override fun run() {
        servers.map(MapleServer::shutdown)
    }
}
