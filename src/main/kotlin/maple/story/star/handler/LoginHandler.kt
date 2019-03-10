package maple.story.star.handler

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
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class LoginHandler : MapleAction {

    /**
     * 连接到服务器
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
     * RSA 验证
     */
    @Action(Recv.RSA_KEY)
    fun rsa(): LoginAuthOutbound {
        val random = Random.nextInt(4, 7) // hard code
        val login = "MapLogin$random"
        return LoginAuthOutbound(login)
    }

    /**
     * 请求连接
     */
    @Action(Recv.REQUEST_CONNECTION)
    fun connection(): ConnectionOutbound = ConnectionOutbound()


    /**
     * 登录
     */
    @Action(Recv.LOGIN)
    fun login(login: LoginInbound): LoginOutbound {
        println("login action called")
        return LoginOutbound("test")
    }
}