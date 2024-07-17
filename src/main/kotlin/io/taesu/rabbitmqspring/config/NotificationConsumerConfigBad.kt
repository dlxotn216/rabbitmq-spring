package io.taesu.rabbitmqspring.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * 좋지 않은 설정 사례
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
// @Configuration
class NotificationConsumerConfigBad {
    @Value("\${notification.exchange}")
    private lateinit var notificationExchange: String

    @Value("\${notification.email.queue-name}")
    private lateinit var emailQueueName: String

    @Value("\${notification.email.routing-key}")
    private lateinit var emailRoutingKey: String

    @Value("\${notification.email.dead.queue-name}")
    private lateinit var emailDeadLetterQueueName: String

    @Value("\${notification.email.dead.routing-key}")
    private lateinit var emailDeadLetterRoutingKey: String

    /**
     * Declare exchange
     */
    @Bean
    fun notificationExchange() = DirectExchange(notificationExchange, true, false)

    /**
     * Email 전송을 위한 Queue
     */
    @Bean
    fun notificationEmailQueue(): Queue {
        return Queue(emailQueueName, true, false, false).apply {
            arguments["x-dead-letter-exchange"] = notificationExchange   // DLX
            arguments["x-dead-letter-routing-key"] = emailDeadLetterRoutingKey    // DLQ
        }
    }

    /**
     * Email 전송을 위한 Queue Binding
     */
    @Bean
    fun notificationEmailQueueBinding(
        notificationExchange: DirectExchange,
        notificationEmailQueue: Queue,
    ): Binding {
        return BindingBuilder.bind(notificationEmailQueue)
            .to(notificationExchange)
            .with(emailRoutingKey)
    }

    /**
     * Email 전송을 위한 Queue의 Dead Letter Queue
     */
    @Bean
    fun notificationEmailDeadLetterQueue(): Queue {
        return Queue(emailDeadLetterQueueName, true, false, false)
    }

    /**
     * Email 전송을 위한 Queue의 Dead Letter Queue Binding
     */
    @Bean
    fun notificationEmailDeadLetterQueueBinding(
        notificationExchange: DirectExchange,
        notificationEmailDeadLetterQueue: Queue,
    ): Binding {
        return BindingBuilder.bind(notificationEmailDeadLetterQueue)
            .to(notificationExchange)
            .with(emailDeadLetterRoutingKey)
    }
}
