package io.taesu.rabbitmqspring

import com.rabbitmq.client.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@RabbitListener(queues = ["\${app.orders.consumer.orders-event.topic}"])
@Component
class OrderCommandConsumer {
    @RabbitHandler
    fun listen(
        message: Message,
        channel: Channel,
        @Payload command: OrderAcceptCommand,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received order accept command: $command")
        channel.basicAck(tag, false)
    }

    @RabbitHandler
    fun listen(
        message: Message,
        channel: Channel,
        @Payload command: OrderCancelCommand,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received order cancel command: $command")
        channel.basicAck(tag, false)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
