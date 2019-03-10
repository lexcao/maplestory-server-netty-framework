package maple.story.star.packet

import io.netty.buffer.Unpooled
import org.assertj.core.api.Assertions
import org.junit.Test

class CommonPacketTest {

    // F5 80 FD 80
    // 00 00 F5 80 packet id
    // 00 00 FD 80 packet length
    // 5: 0101 D: 1101   0101 ^ 1101 = 1000 8
    // 00 00 08 00
    // => 8
    @Test
    fun `length from header`() {
        val bytes = byteArrayOf(
            0xF5.toByte(),
            0x80.toByte(),
            0xFD.toByte(),
            0x80.toByte()
        )
        val header = Unpooled.wrappedBuffer(bytes)
        val id = header.readShortLE().toInt()
        val len = header.readShortLE().toInt()
        val length = id xor len
        Assertions.assertThat(length).isEqualTo(8)
    }
}