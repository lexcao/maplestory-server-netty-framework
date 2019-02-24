package maple.story.star.log

import mu.KLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LoggerTests {

    private companion object : KLogging()

    @Test
    fun `logger with companion object`() {
        val className = this.javaClass.simpleName!!
        assertThat(className.contentEquals(logger.name))
    }

    @Test
    fun `trade rolling in file`() {
        val log = logger("trade")
        log.info("trade log")
    }
}