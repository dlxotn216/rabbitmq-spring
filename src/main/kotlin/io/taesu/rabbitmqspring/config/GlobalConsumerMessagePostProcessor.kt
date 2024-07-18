package io.taesu.rabbitmqspring.config

import io.taesu.rabbitmqspring.*
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
class GlobalConsumerMessagePostProcessor: MessagePostProcessor {
    override fun postProcessMessage(message: Message): Message {
        val type = message.messageProperties.headers["type"] as? String? ?: return message

        val typeId: String = when (type) {
            "accept" -> OrderAcceptCommand::class.java.name
            "cancel" -> OrderCancelCommand::class.java.name
            else -> throw IllegalArgumentException("Unknown type $type")
        }

        message.messageProperties.setHeader("__TypeId__", typeId)
        return message
    }
}
