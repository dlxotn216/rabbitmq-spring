package io.taesu.rabbitmqspring

import com.rabbitmq.client.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@Component
class NotificationDlConsumer {
    @RabbitListener(queues = ["\${app.notification.consumer.notification-email.dlTopic}"])
    fun listenEmailDeadLetter(
        message: Message,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received email dead letter: {}", message.body.decodeToString())
        channel.basicAck(tag, false)
    }

    @RabbitListener(queues = ["\${app.notification.consumer.notification-sms.dlTopic}"])
    fun listenSmsDeadLetter(
        message: Message,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received sms dead letter: {}", message.body.decodeToString())
        channel.basicAck(tag, false)
    }

    @RabbitListener(queues = ["\${app.notification.consumer.notification-kakao.dlTopic}"])
    fun listenKakaoDeadLetter(
        message: Message,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received kakao dead letter: {}", message.body.decodeToString())
        channel.basicAck(tag, false)
    }

    @RabbitListener(queues = ["\${app.notification.consumer.notification-line.dlTopic}"])
    fun listen(
        message: Message,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long,
    ) {
        log.info("Received line dead letter: {}", message.body.decodeToString())
        channel.basicAck(tag, false)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
