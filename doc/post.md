Hi, guys.
I am a Java/Kotlin web developer. And also a player of MapleStory.

I found some maple servers that handled the socket packet with SLEA and MPLEW.

`SLEA = SeekableLittleEndianAccessor`

`MPLEW = MaplePacketLittleEndianWriter`

It is hard to read and code some custom business logic.

So, I try to refactor them recently to make it easier to develop.
All what I have done is for study. I am a web developer and never code for game server before.
I just want to learn what game server will do and how to use Netty.

I am going to use Kotlin, Spring Boot, JPA, MySQL, Redis and Netty to rewrite.
Kotlin is my working language. It is similar to Java, but less code and more grammar cookies.

 What I have done:

* make 5 server for chat, login, cash shop, auction and channel based on Netty.
* make maple packet AES for encrypt and decrypt
* use ByteBuf to write or read bytes instead of SLEA and MPLEW

Here is a snippet shows the different between the way writing a packet in old server and the way  in Bytebuf (handle the packet RSA_KEY for demo)
```
// old
case RSA_KEY:
   c.announce(LoginPacket.getLoginAUTH());
   break;

// LoginPacket.getLoginAUTH()
public static byte[] getLoginAUTH() {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.LOGIN_AUTH.getValue());
    int rand = Randomizer.rand(4, 7);
    mplew.writeMapleAsciiString("MapLogin" + (rand == 0 ? "" : rand));
    mplew.writeInt(DateUtil.getTime());

    return mplew.getPacket();
}
```
```
@Action(Recv.RSA_KEY)
fun rsa(): LoginAuthOutbound {
    val random = Random.nextInt(4, 7) // hard code
    val login = "MapLogin$random"
    return LoginAuthOutbound(login)
}
// LoginAuthOutbound
class LoginAuthOutbound(
    val login: String
) : OutboundMapleMessage {

    override val code: Send = Send.LOGIN_AUTH
    override fun packet(out: ByteBuf) {
        out.writeShortLE(code.id)
        out.writeAscii(login)
        out.writeIntLE(datetime())
    }
    private fun datetime(): Int = SimpleDateFormat("yyyyMMddHH").format(Date()).toInt()
}
``` 

The whole code is at [Github](https://github.com/Clixin/maplestory-server-netty-framework).

Please leave a comment,  PM me or create an issue at Github, if you have any question.


By the way, I am willing to join a team which is looking for a server developer :).


