package maple.story.star.netty.action

import maple.story.star.netty.code.RecvCode

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Action(
    val code: RecvCode
)
