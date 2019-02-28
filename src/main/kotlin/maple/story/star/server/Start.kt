package maple.story.star.server

import maple.story.star.netty.common.MapleServer
import mu.KLogging
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Start(
    val servers: ObjectProvider<MapleServer>,
    val stop: Thread
) {

    private companion object : KLogging()

    @PostConstruct
    fun start() {
        logger.info("start..")

        // TODO initializer load wz to redis
        logger.info("load wz to redis")

        // TODO  start runtime
        logger.info("start runtime")

        logger.info("start server")

        // TODO initialize account
        // TODO 更新日志表 ???

        // TODO timer
        logger.info("start timer")

        // TODO game info
        // TODO game item
        // TODO skills
        // TODO character info
        // TODO rewards
        // TODO 角色卡
        // TODO 竞速排行榜
        // TODO trade

        servers.map(MapleServer::startup)

        // TODO other

        Runtime.getRuntime().addShutdownHook(stop)
        // start finish
    }
}