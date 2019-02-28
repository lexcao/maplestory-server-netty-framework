package maple.story.star.handler

import maple.story.star.message.inbound.LoginInbound
import maple.story.star.message.outbound.LoginOutbound
import maple.story.star.netty.action.Action
import maple.story.star.netty.action.MapleAction
import maple.story.star.netty.code.RecvCode
import org.springframework.stereotype.Service

@Service
class LoginHandler : MapleAction {

    @Action(RecvCode.LOGIN)
    fun login(login: LoginInbound): LoginOutbound {

        return LoginOutbound("test")
    }
}