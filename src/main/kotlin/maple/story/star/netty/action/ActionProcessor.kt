package maple.story.star.netty.action

import maple.story.star.client.MapleClient
import maple.story.star.netty.action.reflect.CallableAction
import maple.story.star.netty.action.resolver.ArgumentResolver
import maple.story.star.netty.domain.MaplePacket
import maple.story.star.netty.extension.hex
import mu.KLogging
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Controller
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

@Controller
class ActionProcessor(
    actionProvider: ObjectProvider<MapleAction>,
    argumentResolvers: ObjectProvider<ArgumentResolver>
) {

    companion object : KLogging()

    final val actions: Map<Int, CallableAction>

    init {
        actions = actionProvider.flatMap { instance ->
            instance::class.memberFunctions.mapNotNull { function ->
                function.findAnnotation<Action>()?.let {
                    CallableAction(
                        id = it.code.id,
                        instance = instance,
                        function = function,
                        resolvers = argumentResolvers
                    )
                }
            }
        }.associateBy(CallableAction::id)
    }

    fun process(
        packet: MaplePacket,
        client: MapleClient
    ): Any {
        val action = actions[packet.id]
        if (action == null) {
            throw UnsupportedOperationException(
                "Unknown packet [$packet] action [$action] "
            )
        }
        logger.info { "process action ${action.id.hex()}" }
        // TODO handle exception
        return action.call(packet.data, client)
    }
}