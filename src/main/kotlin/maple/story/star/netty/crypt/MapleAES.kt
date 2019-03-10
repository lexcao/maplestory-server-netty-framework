package maple.story.star.netty.crypt

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import maple.story.star.constant.MapleVersion
import maple.story.star.netty.domain.IVInfo
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.bytes
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import kotlin.experimental.xor

/**
 *  copy from MapleAESOFB.java
 *  but group send and recv together
 */
class MapleAES {

    // 143 == 8F 00 == 36608
    val sendIV: IVInfo = IVInfo.send()
    // 65392 == 70 FF == 28927
    val recvIV: IVInfo = IVInfo.recv()

    private val cipher = Cipher.getInstance("AES")

    init {
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpecFactory.get(MapleVersion.INT % 20))
    }

    // --------------------- public
    fun valid(packet: MaplePacket): Boolean {
        val id = packet.id
        val iv = recvIV.iv
        val version = recvIV.version
        // [69. 69] packet id
        // FIXME just for packet id
        val packetArray = intArrayOf(
            (id shr 8) and 0xFF,
            (id shr 0) and 0xFF
        )
        val a1 = (packetArray[0] xor iv[2]) and 0xFF

        val a2 = (version shr 8) and 0xFF

        val b1 = (packetArray[1] xor iv[3]) and 0xFF

        val b2 = version and 0xFF

        return a1 == a2 && b1 == b2
    }


    fun encrypt(data: ByteBuf): ByteBuf =
        crypt(sendIV, data)

    fun decrypt(data: ByteBuf): ByteBuf =
        crypt(recvIV, data)

    fun generateHeader(length: Int): ByteArray {
        val iv = sendIV.iv
        val version = sendIV.version
        val iiv = iv[3] and 0xFF or (iv[2] shl 8 and 0xFF00) xor version
        val mlength = length shl 8 and 0xFF00 or length.ushr(8) xor iiv

        return byteArrayOf(
            (iiv.ushr(8) and 0xFF).toByte(),
            (iiv and 0xFF).toByte(),
            (mlength.ushr(8) and 0xFF).toByte(),
            (mlength and 0xFF).toByte()
        )
    }

    // --------------------- private
    private fun crypt(ivInfo: IVInfo, data: ByteBuf): ByteBuf {
        val iv = ivInfo.iv
        val origin = data.bytes()
        var remaining = origin.size
        var length = 0x5B0
        var start = 0

        try {
            while (remaining > 0) {
                val myIv = multiplyBytes(iv, 4, 4)
                if (remaining < length) {
                    length = remaining
                }

                for (pos in start until start + length) {
                    val index = (pos - start) % myIv.size

                    if (index == 0) {
                        val newIv = cipher.doFinal(myIv)
                        newIv.copyInto(myIv, 0, 0, myIv.size)
                    }
                    origin[index] = origin[index] xor myIv[index]
                }
                start += length
                remaining -= length
                length = 0x5B4
            }
            ivInfo.updateIV()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }

        return Unpooled.wrappedBuffer(origin)
    }

    private operator fun ByteBuf.get(index: Int): Byte =
        this.getByte(index)

    private operator fun ByteBuf.set(index: Int, value: Byte) {
        this.setByte(index, value.toInt())
    }

    private fun multiplyBytes(
        iv: IntArray,
        count: Int,
        multi: Int
    ): ByteArray {
        val size = count * multi
        val result = IntArray(size)
        for (x in 0 until size) {
            result[x] = iv[x % count]
        }
        return result.map(Int::toByte).toByteArray()
    }
}