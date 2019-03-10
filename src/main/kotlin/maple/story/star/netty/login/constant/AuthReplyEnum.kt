package maple.story.star.netty.login.constant

enum class AuthReplyEnum(val code: Int) {

    LOGIN_SUCCESSFUL(0),
    ACCOUNT_DELETE(3),
    PASSWORD_ERROR(4),
    ACCOUNT_NOT_LANDED(5),
    SYSTEM_ERROR(6),
    CONNECTING_ACCOUNT(7),
    CONNECTION_BUSY(10),
    CONNECTION_LOCKING(13),
    DEFINITION_INFO(16),
    PROTOCOL_INFO(22);
}