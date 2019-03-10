package maple.story.star.bean

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.getBean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
object BeanFactory : BeanFactoryAware {

    lateinit var factory: BeanFactory

    override fun setBeanFactory(beanFactory: BeanFactory) {

        factory = beanFactory
    }

    inline fun <reified T : Any> get(): T = this.factory.getBean()
}
