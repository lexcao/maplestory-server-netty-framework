package maple.story.star.client

import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.util.AttributeKey
import maple.story.star.netty.crypt.MapleAES
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.bytes
import maple.story.star.netty.login.constant.AuthReplyEnum
import kotlin.random.Random

class MapleClient(
    val session: Channel,
    val channelId: Int = -1,
    val worldId: Int = -1
) {
    private val aes: MapleAES = MapleAES()
    private val sessionId: Long = Random.nextLong()
    var mac: String = ""
    var username: String = ""
    var password: String = ""

    companion object {
        val CLIENT: AttributeKey<MapleClient> = AttributeKey.newInstance("CLIENT")
    }

    var accountId: Int = -1

    var login: Boolean = false
        get() = field && accountId > 0
    var receiving: Boolean = true

    fun valid(packet: MaplePacket): Boolean = aes.valid(packet)
    fun encrypt(data: ByteBuf): ByteBuf = aes.encrypt(data)
    fun decrypt(data: ByteBuf): ByteBuf = aes.decrypt(data)
    fun header(length: Int): ByteArray = aes.generateHeader(length)
    fun sendIV(): ByteArray = aes.sendIV.iv.bytes()
    fun recvIV(): ByteArray = aes.recvIV.iv.bytes()

    fun disconnect(
        inChannelServer: Boolean,
        inCashServer: Boolean,
        allShutdown: Boolean
    ) {
        // 断线太多逻辑，卧槽
    }

    fun login() {

    }

    fun doLogin(): AuthReplyEnum {
        var init = AuthReplyEnum.ACCOUNT_NOT_LANDED

        return AuthReplyEnum.LOGIN_SUCCESSFUL
    }

}