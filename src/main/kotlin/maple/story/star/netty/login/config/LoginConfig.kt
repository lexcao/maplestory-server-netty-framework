package maple.story.star.netty.login.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:config/server/login.yml", encoding = "UTF-8")
@ConfigurationProperties
class LoginConfig {

    var port: Int = 6001

    var version: Int = 143

    var type: Int = 4

    var name: String = "star maplestory"

    var eventMessage: String = "Have fun!"

    var message: String = "游戏置顶公告"

    var flag: Int = 0

    var status: Int = 3

    var maxUserLimit: Int = 100

    var defaultUserLimit: Int = 100

    var enableUserHash: Boolean = true

    var enableDeleteCharacter: Boolean = false
}