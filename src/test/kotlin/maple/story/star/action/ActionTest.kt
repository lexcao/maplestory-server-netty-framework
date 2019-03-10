package maple.story.star.action

import io.netty.buffer.Unpooled
import io.netty.channel.embedded.EmbeddedChannel
import maple.story.star.client.MapleClient
import maple.story.star.code.Recv
import maple.story.star.netty.action.ActionProcessor
import maple.story.star.netty.action.reflect.CallableAction
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class ActionTest {

    @Autowired
    lateinit var actionProcessor: ActionProcessor
    lateinit var actions: Map<Int, CallableAction>

    @Before
    fun before() {
        actions = actionProcessor.actions
    }

    private val loginPacket = byteArrayOf(
        0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0x07, 0, 0x72, 0x6F, 0x6F, 0x74, 0x31, 0x32, 0x33,
        0x07, 0, 0x72, 0x6F, 0x6F, 0x74, 0x31, 0x32, 0x33
    )

    @Test
    fun `it works`() {
        val data = Unpooled.wrappedBuffer(loginPacket)
        val client = MapleClient(session = EmbeddedChannel())
        actions[Recv.UNKNOWN.id]!!.call(data.duplicate(), client)
        actions[0x6969]!!.call(data.duplicate(), client)
        actions[Recv.NOTE_ACTION.id]!!.call(data.duplicate(), client)
        actions[Recv.ADD_ATTACK_RESET.id]!!.call(data.duplicate(), client)
        actions[Recv.AUTO_AGGRO.id]!!.call(data.duplicate(), client)
    }
}