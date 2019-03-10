package maple.story.star.controller

import maple.story.star.code.Recv
import maple.story.star.message.inbound.ClientErrorMessageInbound
import maple.story.star.netty.action.Action
import maple.story.star.netty.action.MapleAction
import org.springframework.stereotype.Controller

@Controller
class ErrorController : MapleAction {

    @Action(Recv.CLIENT_ERROR)
    fun clientError(message: ClientErrorMessageInbound) {
        println(message)
    }

    @Action(Recv.UNKNOWN)
    fun unknown(message: ClientErrorMessageInbound) {
        println(message)
    }
}