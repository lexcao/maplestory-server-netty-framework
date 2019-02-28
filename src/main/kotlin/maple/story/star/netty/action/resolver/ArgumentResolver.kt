package maple.story.star.netty.action.resolver

import io.netty.buffer.ByteBuf
import maple.story.star.client.MapleClient
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

interface ArgumentResolver {

    fun supports(param: KParameter): Boolean

    fun resolve(
        param: KParameter,
        data: ByteBuf,
        client: MapleClient,
        constructor: KFunction<*>?
    ): Any?
}