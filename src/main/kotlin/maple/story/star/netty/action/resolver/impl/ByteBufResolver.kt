package maple.story.star.netty.action.resolver.impl

import io.netty.buffer.ByteBuf
import maple.story.star.client.MapleClient
import maple.story.star.netty.action.resolver.ArgumentResolver
import org.springframework.stereotype.Component
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

@Component
class ByteBufResolver : ArgumentResolver {

    override fun supports(param: KParameter): Boolean {
        return param.type.jvmErasure == ByteBuf::class
    }

    override fun resolve(
        param: KParameter,
        data: ByteBuf,
        client: MapleClient,
        constructor: KFunction<*>?
    ): Any = data
}