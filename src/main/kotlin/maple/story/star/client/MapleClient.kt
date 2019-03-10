package maple.story.star.client

import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.util.AttributeKey
import maple.story.star.netty.crypt.MapleAES
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.bytes
import maple.story.star.netty.login.constant.AuthReplyEnum
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random

class MapleClient(
    private val session: Channel,
    val channelId: Int = -1,
    val worldId: Int = -1
) {
    private val aes: MapleAES = MapleAES()
    private val sessionId: Long = Random.nextLong()
    private val lock: Lock = ReentrantLock(true)
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

    /**
     *  method for AES
     */
    fun valid(packet: MaplePacket): Boolean = aes.valid(packet)

    fun encrypt(data: ByteBuf): ByteBuf = aes.encrypt(data)
    fun decrypt(data: ByteBuf): ByteBuf = aes.decrypt(data)
    fun header(length: Int): ByteArray = aes.generateHeader(length)
    fun sendIV(): ByteArray = aes.sendIV.iv.bytes()
    fun recvIV(): ByteArray = aes.recvIV.iv.bytes()

    /**
     *  method for Channel
     */
    fun send(data: ByteBuf) {
        session.writeAndFlush(data)
    }

    /**
     *  self method
     */
    fun lock(block: () -> Unit) {
        lock.lock()
        try {
            block()
        } finally {
            lock.unlock()
        }
    }

    fun close() {
        session.close()
    }

    fun disconnect(
        inChannelServer: Boolean,
        inCashServer: Boolean,
        allShutdown: Boolean
    ) {
        TODO("$inCashServer,$inChannelServer,$allShutdown")
        // 断线太多逻辑，卧槽
    }

    fun login() {

    }

    fun doLogin(): AuthReplyEnum {
        val init = AuthReplyEnum.ACCOUNT_NOT_LANDED
        println(init)
        return AuthReplyEnum.LOGIN_SUCCESSFUL
    }

}