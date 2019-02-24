package maple.story.star.binary

import maple.story.star.netty.extension.shortLE
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BinaryTest {

    @Test
    fun `int to short LE`() {
        val recvVersion = 143
        val recvOld = (recvVersion shr 8) and 0xFF or ((recvVersion shl 8) and 0xFF00)
        val recvNew = recvVersion.shortLE()

        val sendVersion = 0xFFFF - recvVersion
        val sendOld = (sendVersion shr 8) and 0xFF or ((sendVersion shl 8) and 0xFF00)
        val sendNew = sendVersion.shortLE()

        assertThat(recvOld).isEqualTo(36608).isEqualTo(0x8F00)
        assertThat(recvNew).isEqualTo(recvOld)

        assertThat(sendOld).isEqualTo(28927).isEqualTo(0x70FF)
        assertThat(sendNew).isEqualTo(sendOld)
    }
}