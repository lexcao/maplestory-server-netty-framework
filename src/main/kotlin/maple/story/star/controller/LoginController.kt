package maple.story.star.controller

import maple.story.star.client.MapleClient
import maple.story.star.code.Recv
import maple.story.star.constant.MapleVersion
import maple.story.star.message.inbound.ClientHelloInbound
import maple.story.star.message.inbound.LoginInbound
import maple.story.star.message.outbound.ConnectionOutbound
import maple.story.star.message.outbound.LoginAuthOutbound
import maple.story.star.message.outbound.LoginOutbound
import maple.story.star.netty.action.Action
import maple.story.star.netty.action.MapleAction
import org.springframework.stereotype.Controller
import kotlin.random.Random

@Controller
class LoginController : MapleAction {

    /**
     *  connect to server
     */
    @Action(Recv.CLIENT_HELLO)
    fun hello(hello: ClientHelloInbound, client: MapleClient) {
        val version = hello.mapleVersion == MapleVersion.INT
        val type = hello.mapleType == MapleVersion.LOGIN_MAPLE_TYPE
        val patch = hello.patchString == MapleVersion.MAPLE_PATCH_STRING

        if (!version || !type || !patch)
            client.close()
    }

    /**
     *  auth with rsa
     */
    @Action(Recv.RSA_KEY)
    fun rsa(): LoginAuthOutbound {
        val random = Random.nextInt(4, 7) // hard code
        val login = "MapLogin$random"
        return LoginAuthOutbound(login)
    }

    /**
     *  request connection
     */
    @Action(Recv.REQUEST_CONNECTION)
    fun connection(): ConnectionOutbound = ConnectionOutbound()


    /**
     *  login
     */
    @Action(Recv.LOGIN)
    fun login(login: LoginInbound): LoginOutbound {
        TODO("login logic")
    }
}