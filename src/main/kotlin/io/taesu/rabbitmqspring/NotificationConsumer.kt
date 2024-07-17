package io.taesu.rabbitmqspring

import com.rabbitmq.client.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
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
@Component
class NotificationConsumer {
    @RabbitListener(queues = ["\${app.notification.consumer.notification-email.topic}"])
    fun listen(
        message: Message,
        channel: Channel,
        @Payload payload: EmailNotificationCommand,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received email notification command: {}", payload)
        channel.basicAck(tag, false)
    }

    @RabbitListener(queues = ["\${app.notification.consumer.notification-sms.topic}"])
    fun listen(
        message: Message,
        channel: Channel,
        @Payload payload: SmsNotificationCommand,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received sms notification command: {}", payload)
        channel.basicAck(tag, false)
    }

    @RabbitListener(queues = ["\${app.notification.consumer.notification-kakao.topic}"])
    fun listen(
        message: Message,
        channel: Channel,
        @Payload payload: KakaoNotificationCommand,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received kakao notification command: {}", payload)
        channel.basicAck(tag, false)
    }

    @RabbitListener(queues = ["\${app.notification.consumer.notification-line.topic}"])
    fun listen(
        message: Message,
        channel: Channel,
        @Payload payload: LineNotificationCommand,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received line notification command: {}", payload)
        channel.basicAck(tag, false)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
