package maple.story.star.netty.action

import io.netty.buffer.ByteBuf
import maple.story.star.client.MapleClient
import maple.story.star.code.Recv
import maple.story.star.message.inbound.LoginInbound
import maple.story.star.message.outbound.LoginOutbound

//@Service
class TestAction : MapleAction {

    @Action(Recv.UNKNOWN)
    fun test() {
        println("-=-=-=-=-=-= test  call -=-==-=-=-=")
    }

    @Action(Recv.LOGIN)
    fun login(login: LoginInbound): LoginOutbound {

        println(login)

        return LoginOutbound(
            id = "test"
        )
    }

    @Action(Recv.NOTE_ACTION)
    fun testData(data: ByteBuf) {
        println("data")
        println(data)
    }

    @Action(Recv.ADD_ATTACK_RESET)
    fun testBoth(login: LoginInbound, data: ByteBuf) {
        println("both")
        println(login)
        println(data)
    }

    @Action(Recv.AUTO_AGGRO)
    fun testBothReverse(data: ByteBuf, client: MapleClient, login: LoginInbound) {
        println("triple")
        println(login)
        println(data)
    }
}