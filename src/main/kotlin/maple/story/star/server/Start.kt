package maple.story.star.server

import maple.story.star.netty.login.LoginServer
import mu.KLogging
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Start(
    val loginServer: LoginServer
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

        logger.info("[server-login] starting")
        loginServer.startup()
        logger.info("[server-login] finish start")
        // TODO get server
        // TODO cash shop server
        // TODO trade server
        // TODO chat server

        // TODO other

    }
}