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
 * Created by itaesu on 2024. 7. 17..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@Configuration
class OrdersConsumerConfig(private val properties: OrderConsumerProperties) {
    @Bean
    fun ordersDeclarables(): Declarables {
        val exchange = DirectExchange(properties.exchange, true, false)
        val queues = properties.instanceValues.map {
            it.routingKey to Queue(it.topic, true, false, false).apply {
                it.dlRoutingKey ?: return@apply
                arguments["x-dead-letter-exchange"] = properties.exchange   // DLX
                arguments["x-dead-letter-routing-key"] = it.dlRoutingKey    // DLQ
            }
        }

        val dlQueues = properties.instanceValues.mapNotNull {
            it.dlTopic ?: return@mapNotNull null
            it.dlRoutingKey ?: return@mapNotNull null
            it.dlRoutingKey to Queue(it.dlTopic, true, false, false)
        }
        val queueMap = (queues + dlQueues).toMap()

        val queueBindings = properties.instanceValues.mapNotNull {
            val queue = queueMap[it.routingKey] ?: return@mapNotNull null
            BindingBuilder.bind(queue)
                .to(exchange)
                .with(it.routingKey)
        }

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

    @Bean
    fun rabbitListenerContainerFactory(
        configurer: SimpleRabbitListenerContainerFactoryConfigurer,
        connectionFactory: ConnectionFactory,
        objectMapper: ObjectMapper,
    ): SimpleRabbitListenerContainerFactory {
        return SimpleRabbitListenerContainerFactory().apply {
            configurer.configure(this, connectionFactory)
            this.setMessageConverter(Jackson2JsonMessageConverter(objectMapper))
            this.setAfterReceivePostProcessors(GlobalConsumerMessagePostProcessor())
            this.setAcknowledgeMode(AcknowledgeMode.MANUAL)
            this.setPrefetchCount(1)
        }
    }
}
