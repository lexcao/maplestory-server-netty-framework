package maple.story.star.netty.action

import io.netty.buffer.ByteBuf
import maple.story.star.client.MapleClient
import maple.story.star.message.inbound.LoginInbound
import maple.story.star.message.outbound.LoginOutbound
import maple.story.star.netty.code.RecvCode
import org.springframework.stereotype.Service

@Service
class TestAction : MapleAction {

    @Action(RecvCode.UNKNOWN)
    fun test() {
        println("-=-=-=-=-=-= test  call -=-==-=-=-=")
    }

    @Action(RecvCode.LOGIN)
    fun login(login: LoginInbound): LoginOutbound {

        println(login)

        return LoginOutbound(
            id = "test"
        )
    }

    @Action(RecvCode.NOTE_ACTION)
    fun testData(data: ByteBuf) {
        println("data")
        println(data)
    }

    @Action(RecvCode.ADD_ATTACK_RESET)
    fun testBoth(login: LoginInbound, data: ByteBuf) {
        println("both")
        println(login)
        println(data)
    }

    @Action(RecvCode.AUTO_AGGRO)
    fun testBothReverse(data: ByteBuf, client: MapleClient, login: LoginInbound) {
        println("triple")
        println(login)
        println(data)
    }
}