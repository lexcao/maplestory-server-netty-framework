package maple.story.star.netty.extension

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.util.AsciiString
import io.netty.util.internal.StringUtil
import maple.story.star.constant.ServerConst


fun ByteBuf.compact(
    length: Int = readableBytes()
): ByteBuf = readRetainedSlice(length)

fun ByteBuf.print(): String =
    ByteBufUtil.prettyHexDump(this)

fun ByteBuf.hex(): String =
    StringUtil.toHexStringPadded(bytes()).toUpperCase()

fun ByteBuf.bytes(): ByteArray =
    ByteBufUtil.getBytes(this)

fun ByteBuf.writeAscii(seq: String): ByteBuf {
    this.writeShortLE(seq.length)
    val ascii = AsciiString(seq.toByteArray(ServerConst.CHARSET))
    ByteBufUtil.writeAscii(this, ascii)
    return this
}

fun ByteBuf.readAscii(): String {
    val length = this.readShortLE().toInt()
    return readCharSequence(length, ServerConst.CHARSET).toString()
}

fun ByteBuf.end() {
    writeByte(1)
}

fun Int.hex(): String = Integer.toHexString(this).toUpperCase()

fun Int.shortLE(): Int =
    ((this shr 8) and 0xFF or ((this shl 8) and 0xFF00)).toShort().toInt()

fun IntArray.bytes(): ByteArray =
    this.map(Int::toByte).toByteArray()
