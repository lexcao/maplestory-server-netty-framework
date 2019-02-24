package maple.story.star.netty.login.constant

enum class AuthReplyEnum(val code: Int) {

    LOGIN_SUCCESSFUL(0), // 登录成功
    ACCOUNT_DELETE(3), // 用户删除
    PASSWORD_ERROR(4), // 密码错误
    ACCOUNT_NOT_LANDED(5), // 还没着陆
    SYSTEM_ERROR(6), // 系统错误
    CONNECTING_ACCOUNT(7), // 正在连接用户
    CONNECTION_BUSY(10), // 连接繁忙
    CONNECTION_LOCKING(13), // 连接阻塞
    DEFINITION_INFO(16), // 定义信息
    PROTOCOL_INFO(22); // 协议信息
}