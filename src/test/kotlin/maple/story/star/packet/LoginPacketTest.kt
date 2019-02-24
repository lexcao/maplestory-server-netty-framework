package maple.story.star.packet

import io.netty.buffer.Unpooled
import io.netty.channel.socket.nio.NioSocketChannel
import maple.story.star.client.MapleClient
import maple.story.star.netty.extension.receive
import maple.story.star.netty.login.LoginPacketHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.random.Random

class LoginPacketTest {

    // 69 69 29 00 00 00 00 00 00 00
    // 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    // 07 00 72 6F 6F 74 31 32 33
    // 00 00 72 6F 6F 74 31 32 33
    private val loginPacket = byteArrayOf(
        0x69, 0x69, 0x29, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0x07, 0, 0x72, 0x6F, 0x6F, 0x74, 0x31, 0x32, 0x33,
        0x07, 0, 0x72, 0x6F, 0x6F, 0x74, 0x31, 0x32, 0x33
    )

    private val commonPacket = byteArrayOf(
        116, 0, 5, 0, 100, 101, 109, 111, 110
    )

    @Test
    fun `ByteBuf to maple common packet`() {
        val inbound = Unpooled.wrappedBuffer(commonPacket)
        val packet = inbound.receive()
        println(packet)
    }

    @Test
    fun `ByteBuf to maple login packet`() {
        val inbound = Unpooled.wrappedBuffer(loginPacket)
        val packet = inbound.receive()
        println(packet)
    }

    @Test
    fun `Response in hello packet`() {
        val ivRecv = intArrayOf(
            70, 114, 122,
            Random.nextBits(Byte.SIZE_BITS)
        )
        val ivSend = intArrayOf(
            82, 48, 120,
            Random.nextBits(Byte.SIZE_BITS)
        )
        val mapleVersion = 143
        LoginPacketHandler.hello(
            MapleClient(session = NioSocketChannel())
        )
    }

    @Test
    fun `mac string from byte buf`() {
        val bytes = byteArrayOf(
//            0, 0, 0, 0, 0, 0
            0x10, 0xBF.toByte(), 0x48, 0x7B, 0x10, 0x8B.toByte()
        )
        val data = Unpooled.wrappedBuffer(bytes)
        val mac = LoginPacketHandler.getMacAddress(data)
        assertThat(mac).isEqualTo(
//            "00-00-00-00-00-00"
            "10-BF-48-7B-10-8B"
        )
    }

    @Test
    fun `username and password from buf`() {
        val bytes = byteArrayOf(
            0x07, 0, 0x72, 0x6F, 0x6F, 0x74, 0x31, 0x32, 0x33,
            0x07, 0, 0x72, 0x6F, 0x6F, 0x74, 0x31, 0x32, 0x33
        )
        val data = Unpooled.wrappedBuffer(bytes)
        val (username, password) = LoginPacketHandler.getLoginInfo(data)

        assertThat(username).isEqualTo("root123")
        assertThat(username).isEqualTo(password)
    }

    fun IntArray.bytes(): ByteArray =
        this.map(Int::toByte).toByteArray()
}