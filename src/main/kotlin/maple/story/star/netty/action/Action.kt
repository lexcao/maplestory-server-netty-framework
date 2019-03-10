package maple.story.star.netty.action

import maple.story.star.code.Recv

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Action(
    val code: Recv
)
