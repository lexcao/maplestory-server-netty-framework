package maple.story.star.netty.crypt

import io.netty.buffer.ByteBuf
import maple.story.star.constant.MapleVersion
import maple.story.star.netty.domain.IVInfo
import maple.story.star.netty.extension.bytes
import maple.story.star.netty.extension.toByteBuf
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import kotlin.experimental.xor

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
    fun validReceivedPacketId(id: Int): Boolean {
        val iv = recvIV.iv
        val version = recvIV.version
        // [69. 69] packet id
        // FIXME just for packet id , need fix
        val packet = intArrayOf(
            (id shr 8) and 0xFF,
            (id shr 0) and 0xFF
        )
        // maple version 143

        // ((((packet[0] ^ iv[2]) & 0xFF) ==
        // packet[0] 69
        // 0110 1001
        // iv[2] 122
        // 0111 1010
        // xor ^
        // 0001 0011
        // and & 1111 1111
        // 19
        val a1 = (packet[0] xor iv[2]) and 0xFF// 19

        // ((mapleVersion >> 8) & 0xFF))
        // version 8F
        // 1000 1111
        // >> 8
        val a2 = (version shr 8) and 0xFF // 0
        // &&

        // (((packet[1] ^ iv[3]) & 0xFF) ==
        // packet[1] 69
        // 0110 1001
        // iv[3] = 33
        // 0011 0011
        // oxr ^
        // 0101 1010
        // and & 1111 1111
        // 0101 1010
        // 0x5A
        val b1 = (packet[1] xor iv[3]) and 0xFF // 0x5A

        // (mapleVersion & 0xFF)));
        val b2 = version and 0xFF // 8F

        val result = a1 == a2 && b1 == b2

        return result
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

        return origin.toByteBuf()
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