package maple.story.star.netty.extension

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import io.netty.util.AsciiString
import maple.story.star.netty.login.LoginPacketHandler


fun ByteBuf.compact(): ByteBuf =
    readSlice(readableBytes())

fun ByteBuf.bytes(): ByteArray =
    ByteBufUtil.getBytes(this)

fun ByteBuf.print(): String =
    ByteBufUtil.prettyHexDump(this)

fun ByteBuf.writeAscii(seq: String): ByteBuf {
    this.writeShortLE(seq.length)
    val ascii = AsciiString(seq.toByteArray(LoginPacketHandler.GBK))
    ByteBufUtil.writeAscii(this, ascii)
    return this
}

fun ByteBuf.readAscii(): String {
    val length = this.readShortLE().toInt()
    return readCharSequence(length, LoginPacketHandler.GBK).toString()
}

fun Int.hex(): String = Integer.toHexString(this)

fun Int.shortLE(): Int =
    ((this shr 8) and 0xFF or ((this shl 8) and 0xFF00)).toShort().toInt()

fun IntArray.bytes(): ByteArray =
    this.map(Int::toByte).toByteArray()

fun ByteArray.toByteBuf(): ByteBuf =
    Unpooled.wrappedBuffer(this)
