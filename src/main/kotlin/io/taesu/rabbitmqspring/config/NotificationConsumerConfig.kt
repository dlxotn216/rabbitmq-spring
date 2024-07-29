package io.taesu.rabbitmqspring.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@Configuration
class NotificationConsumerConfig(private val properties: NotificationConsumerProperties) {
    @Bean
    fun notificationDeclarables(): Declarables {
        // Create Exchange
        val exchange = DirectExchange(properties.exchange, true, false)

        // Create Queue
        // "emai": Queue("app.notification.email")
        // "sms": Queue("app.notification.sms")
        val queues = properties.instanceValues.map {
            it.routingKey to Queue(it.topic, true, false, false).apply {
                it.dlRoutingKey ?: return@apply
                arguments["x-dead-letter-exchange"] = properties.exchange   // DLX
                arguments["x-dead-letter-routing-key"] = it.dlRoutingKey    // DLQ
            }
        }

        // Create DeadLetter Queue
        // "emai.dead": Queue("app.notification.email.dead")
        // "sms.dead": Queue("app.notification.sms.dead")
        val dlQueues = properties.instanceValues.mapNotNull {
            it.dlTopic ?: return@mapNotNull null
            it.dlRoutingKey ?: return@mapNotNull null
            it.dlRoutingKey to Queue(it.dlTopic, true, false, false)
        }
        val queueMap = (queues + dlQueues).toMap()

        // Create Queue Binding
        val queueBindings = properties.instanceValues.mapNotNull {
            val queue = queueMap[it.routingKey] ?: return@mapNotNull null
            BindingBuilder.bind(queue)
                .to(exchange)
                .with(it.routingKey)
        }

        // Create DeadLetter Queue Binding
        val deadLetterQueueBindings = properties.instanceValues.mapNotNull {
            val queue = queueMap[it.dlRoutingKey] ?: return@mapNotNull null
            BindingBuilder.bind(queue)
                .to(exchange)
                .with(it.dlRoutingKey)
        }


        return Declarables(
            exchange,
            *queueMap.values.toTypedArray(),
            *(queueBindings + deadLetterQueueBindings).toTypedArray()
        )
    }
}
