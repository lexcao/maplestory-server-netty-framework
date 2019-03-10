package maple.story.star.controller

import maple.story.star.client.MapleClient
import maple.story.star.constant.MapleVersion
import maple.story.star.message.outbound.HelloOutbound

/**
 *  handle channel active
 *  send AES IV(initial vector) to maple client
 *  used for packet encrypt and decrypt
 */
object SessionController {

    fun hello(
        client: MapleClient
    ): HelloOutbound = HelloOutbound(
        length = 16,
        mapleVersion = MapleVersion.INT,
        patchString = "1",
        client = client,
        loginMapleType = MapleVersion.LOGIN_MAPLE_TYPE
    )
}