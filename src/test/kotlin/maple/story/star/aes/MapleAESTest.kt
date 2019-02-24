package maple.story.star.aes

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import maple.story.star.constant.MapleVersion
import maple.story.star.netty.crypt.MapleAES
import maple.story.star.netty.extension.bytes
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MapleAESTest {

    val ivRecv = intArrayOf(
        70, 114, 122,
        99
    )
    val ivSend = intArrayOf(
        82, 48, 120,
        99
    )
    val version = MapleVersion.INT

    val sendMessage: ByteBuf = Unpooled.wrappedBuffer(
        byteArrayOf(116, 0, 5, 0, 100, 101, 109, 111, 110)
    )

    val recvMessage: ByteBuf = Unpooled.wrappedBuffer(
        byteArrayOf(-66, -116, 78, -13, 43, 43, -101, -91, -23)
    )

    val new = MapleAES()
    val oldSend = OldAES(new.sendIV.iv.bytes(), (0xFFFF - version).toShort())
    val oldRecv = OldAES(new.recvIV.iv.bytes(), version.toShort())

    private fun IntArray.bytes(): ByteArray =
        map(Int::toByte).toByteArray()

    @Test
    fun getPacketLength() {
        val length = 67
        val new = new.generateHeader(length)
        val old = oldSend.getPacketHeader(length)

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
        val newCheck = new.validReceivedPacketId(packetId)
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
        assertThat(newIV).isEqualTo(oldIV)
        val newDecrypt = new.encrypt(sendMessage).bytes()
        decrypt(newDecrypt)
        val oldDecrypt = oldSend.crypt(sendMessage.bytes())
        decrypt(oldDecrypt)
    }

    @Test
    fun decrypt() {
        val newDecrypt = new.decrypt(recvMessage).bytes()
        val oldDecrypt = oldRecv.crypt(recvMessage.bytes())
        assertThat(newDecrypt).isEqualTo(oldDecrypt)
    }

    fun decrypt(data: ByteArray) {
        val newDecrypt = new.decrypt(Unpooled.wrappedBuffer(data)).bytes()
        val oldDecrypt = oldRecv.crypt(data)
        assertThat(newDecrypt).isEqualTo(oldDecrypt)
    }
}