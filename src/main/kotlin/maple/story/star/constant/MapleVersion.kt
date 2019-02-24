package maple.story.star.constant

import maple.story.star.netty.extension.shortLE


object MapleVersion {

    const val INT: Int = 143
    val SHORT: Int = INT.shortLE()
    val SHORT_REVERSE: Int = (0xFFFF - INT).shortLE()

    const val LOGIN_MAPLE_TYPE: Int = 4
}