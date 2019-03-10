package maple.story.star.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class TimeTest {

    @Test
    fun `datetime format`() {
        assertThat(
            SimpleDateFormat("yyyyMMddHH").format(Date()).toInt()
        ).isEqualTo(2019030211)
    }
}