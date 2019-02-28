package maple.story.star.netty.action.reflect

import io.netty.buffer.ByteBuf
import maple.story.star.client.MapleClient
import maple.story.star.message.inbound.InboundMapleMessage
import maple.story.star.netty.action.MapleAction
import maple.story.star.netty.action.resolver.ArgumentResolver
import org.springframework.beans.factory.ObjectProvider
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

class CallableAction(
    val id: Int,
    private val instance: MapleAction,
    private val function: KFunction<*>,
    private val resolvers: ObjectProvider<ArgumentResolver>
) {

    private val messageType: KParameter?
    private val messageConstructor: KFunction<*>?
    private val parameters = function.valueParameters

    init {
        messageType = parameters.firstOrNull(::message)
        messageConstructor = (messageType?.type?.classifier as? KClass<*>)?.primaryConstructor
    }

    fun call(
        data: ByteBuf,
        client: MapleClient
    ): Any {

        val args = parameters.mapNotNull { param ->
            resolvers.find { it.supports(param) }
                ?.resolve(param, data, client, messageConstructor)
        }.toTypedArray()

        return doCall(*args)
    }

    private fun message(param: KParameter): Boolean =
        param.type.jvmErasure.isSubclassOf(InboundMapleMessage::class)

    private fun doCall(
        vararg args: Any
    ): Any = try {
        function.call(instance, *args) as Any
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}
