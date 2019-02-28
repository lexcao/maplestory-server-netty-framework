package maple.story.star.config

//@Configuration
//@PropertySource("classpath:config/server/needLogin.yml")
//@ConfigurationProperties
class ServerConfig {

    lateinit var name: String
    lateinit var description: String

    val admin = Admin()

    class Admin {

        lateinit var name: String
    }
}
