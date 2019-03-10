package maple.story.star.aes

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import io.netty.util.internal.StringUtil
import maple.story.star.constant.MapleVersion
import maple.story.star.netty.crypt.MapleAES
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.bytes
import maple.story.star.netty.extension.compact
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.random.Random

class MapleAESTest {

    val ivRecv = intArrayOf(
        70, 114, 122,
        -95
    )
    val ivSend = intArrayOf(
        82, 48, 120,
        -110
    )
    val version = MapleVersion.INT

    val sendMessage: ByteBuf = Unpooled.wrappedBuffer(
//        byteArrayOf(39, 0, 1)
        StringUtil.decodeHexDump("39 00 09 00 4D 61 70 4C 6F 67 69 6E 37 C6 F4 57 78".replace(" ", ""))
//        byteArrayOf(116, 0, 5, 0, 100, 101, 109, 111, 110)
    )

    val originRecv = StringUtil.decodeHexDump("3f0514f06cb5a8c9a7e6a5e65b73")

    val recvMessage: ByteBuf = Unpooled.wrappedBuffer(
        StringUtil.decodeHexDump("3f0514f06cb5a8c9a7e6a5e65b73")
//        byteArrayOf(-66, -116, 78, -13, 43, 43, -101, -91, -23)
    )

    val new = MapleAES()
    val oldSend = OldAES(new.sendIV.iv.bytes(), (0xFFFF - version).toShort())
    val oldRecv = OldAES(new.recvIV.iv.bytes(), version.toShort())

    private fun IntArray.bytes(): ByteArray =
        map(Int::toByte).toByteArray()

    @Test
    fun getPacketLength() {
        val length = sendMessage.readableBytes()
        val new = new.generateHeader(length)
        val old = oldSend.getPacketHeader(length)
        println("new = ${new.print()}")
        println("old = ${old.print()}")
        assertThat(new).isEqualTo(old)
    }

    @Test
    fun checkMapleVersion() {
        val newSendVersion = new.sendIV.version
        val newRecvVersion = new.recvIV.version
        val oldSendVersion = oldSend.mapleVersion.toInt()
        val oldRecvVersion = oldRecv.mapleVersion.toInt()
        assertThat(newRecvVersion).isEqualTo(oldRecvVersion)
        assertThat(newRecvVersion).isEqualTo(MapleVersion.SHORT)
        assertThat(newSendVersion).isEqualTo(MapleVersion.SHORT_REVERSE)
        assertThat(newSendVersion).isEqualTo(oldSendVersion)
    }

    @Test
    fun checkPacket() {
        recvMessage.markReaderIndex()
        val header = recvMessage.readInt()
        recvMessage.resetReaderIndex()
        val packetId = recvMessage.readShortLE().toInt()
        val old = oldRecv.checkPacket(header)
        val newCheck = new.valid(
            MaplePacket(
                id = packetId,
                data = recvMessage.compact()
            )
        )
        assertThat(newCheck).isEqualTo(old)
        assertThat(newCheck).isTrue()
    }

    @Test
    fun updateIv() {
        new.sendIV.updateIV()
        val newSendIv = new.sendIV.iv.bytes()
        oldSend.updateIv()
        val oldSendIv = oldSend.iv
        assertThat(newSendIv).isEqualTo(oldSendIv)

        new.recvIV.updateIV()
        val newRecvIv = new.recvIV.iv.bytes()
        oldRecv.updateIv()
        val oldRecvIv = oldRecv.iv
        assertThat(newRecvIv).isEqualTo(oldRecvIv)
    }

    @Test
    fun encrypt() {
        val newIV = new.sendIV.iv.bytes()
        val oldIV = oldSend.iv
//        val origin = sendMessage.bytes()
        assertThat(newIV).isEqualTo(oldIV)

        println(recvMessage.bytes().print())
        println("origin = ${originRecv.print()}")

        println("newSend = ${new.sendIV.iv.bytes().print()}")
        val newEncrypted = new.encrypt(recvMessage).bytes()
        println("newSend = ${new.sendIV.iv.bytes().print()}")
        new.decrypt(Unpooled.wrappedBuffer(newEncrypted)).bytes()
        println("newSend = ${new.sendIV.iv.bytes().print()}")

        println("oldSend = ${oldSend}")
        val oldEncrypted = oldSend.crypt(originRecv)
        println("oldSend = ${oldSend}")
        val oldDecrypted = oldRecv.crypt(oldEncrypted)
        println("oldSend = ${oldSend}")
        println("== == = = = = = = = = = = =  ==  == = = = ")
        println("oldDecrypted = ${oldDecrypted}")
        println("originRecv = ${originRecv}")
    }

    @Test
    fun oldAES() {
        println("oldSend = ${oldSend}")
        println("origin = ${originRecv.print()}")
        val oldEncrypted = oldSend.crypt(originRecv)
        println("oldSend = ${oldSend}")
        println("oldEncrypted = ${oldEncrypted.print()}")
        val oldDecrypted = oldRecv.crypt(oldEncrypted)
        println("oldSend = ${oldSend}")
        println("oldDecrypted = ${oldDecrypted.print()}")
        assertThat(oldDecrypted).isEqualTo(originRecv)
    }

    @Test
    fun decrypt() {
        val newDecrypt = new.decrypt(recvMessage).bytes()
        val oldDecrypt = oldRecv.crypt(recvMessage.bytes())
        assertThat(newDecrypt).isEqualTo(oldDecrypt)
    }

    fun ByteArray.print(): String = this.contentToString()

    @Test
    fun random() {
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
        println(Random.nextInt(255).toByte())
    }

    @Test
    fun `bytes to ByteBuf then back to bytes`() {
        val origin = "3f0514f06cb5a8c9a7e6a5e65b73"
        val originByteArray = StringUtil.decodeHexDump(origin)
        println("originRecv = ${originByteArray.print()}")
        val byteBuf = Unpooled.wrappedBuffer(originByteArray)
        println("byteBuf = ${ByteBufUtil.hexDump(byteBuf)}")
        val bytes = ByteBufUtil.getBytes(byteBuf)
        println("bytes = ${bytes.print()}")
    }
}