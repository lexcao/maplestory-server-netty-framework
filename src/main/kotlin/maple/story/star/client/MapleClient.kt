package maple.story.star.client

import io.netty.channel.Channel
import io.netty.util.AttributeKey
import maple.story.star.netty.crypt.MapleAES
import maple.story.star.netty.login.constant.AuthReplyEnum
import org.springframework.context.annotation.Scope
import kotlin.random.Random

@Scope("SCOPE_")

class MapleClient(
    val session: Channel,
    val channelId: Int = -1,
    val worldId: Int = -1
) {
    val AES: MapleAES = MapleAES()
    private val sessionId: Long = Random.nextLong()
    var mac: String = ""
    var username: String = ""
    var password: String = ""

    companion object {
        val CLIENT_KEY: AttributeKey<MapleClient> = AttributeKey.newInstance("CLIENT")
    }

    var accountId: Int = -1
    var loggedIn: Boolean = false

    fun isLoggedIn(): Boolean = loggedIn && accountId > 0

    fun disconnect(
        inChannelServer: Boolean,
        inCashServer: Boolean,
        allShutdown: Boolean
    ) {
        // 断线太多逻辑，卧槽
    }


    fun isReceiving(): Boolean {
        return false
    }

    fun login() {

    }

    fun doLogin(): AuthReplyEnum {
        var init = AuthReplyEnum.ACCOUNT_NOT_LANDED

        return AuthReplyEnum.LOGIN_SUCCESSFUL
    }

}