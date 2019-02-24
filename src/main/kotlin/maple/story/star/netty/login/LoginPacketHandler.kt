package maple.story.star.netty.login

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.util.internal.StringUtil
import maple.story.star.client.MapleClient
import maple.story.star.constant.MapleVersion
import maple.story.star.netty.extension.bytes
import maple.story.star.netty.extension.compact
import maple.story.star.netty.extension.print
import maple.story.star.netty.extension.readAscii
import maple.story.star.netty.extension.writeAscii
import mu.KLogging
import java.nio.charset.Charset

object LoginPacketHandler : KLogging() {

    val GBK: Charset = Charset.forName("GBK")

    fun hello(
        client: MapleClient
    ): ByteBuf {
        val buffer = Unpooled.buffer(32)
        buffer.writeShortLE(15) // length
            .writeShortLE(MapleVersion.INT) // version 143
            .writeAscii("1") // patch string

            .writeBytes(client.AES.recvIV.iv.bytes()) // receive iv
            .writeBytes(client.AES.sendIV.iv.bytes()) // send iv
            .writeByte(MapleVersion.LOGIN_MAPLE_TYPE)

            .writeByte(1) // end

        logger.info("[hello] packet \n{}", buffer.print())

        return buffer.compact()
    }


    /**
     *
     * |			mac				 |
     * [      00 00 00 00 00 00      ]
     *
     * | 				   	skip 15	     	 		  |
     * [00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ]
     *
     * | 		    username		  |
     * | length |  r  o  o  t  1  2  3|
     * [  07 00 | 72 6F 6F 74 31 32 33]
     *
     * | 		    password	      |
     * | length |  r  o  o  t  1  2  3|
     * [  00 00 | 72 6F 6F 74 31 32 33]
     */
    fun login(
        data: ByteBuf,
        client: MapleClient
    ) {
        // mac address
        client.mac = getMacAddress(data)
        data.skipBytes(15)

        val (username, password) = getLoginInfo(data)

        client.username = username
        client.password = password

        // TODO check client is banned
        // TODO handle temp ban
        client.login()
    }

    fun getLoginInfo(data: ByteBuf): Pair<String, String> {
        val username = data.readAscii()
        // todo fix password length with empty
        val password = data.readAscii()
        return Pair(username, password)
    }

    fun getMacAddress(data: ByteBuf): String {
        val bytes = IntArray(6)
        for (i in bytes.indices) {
            bytes[i] = data.readByte().toInt()
        }

        return bytes.joinToString("-") {
            StringUtil.byteToHexStringPadded(it).toUpperCase()
        }
    }
}