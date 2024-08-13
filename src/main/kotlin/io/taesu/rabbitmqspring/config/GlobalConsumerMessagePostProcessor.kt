package io.taesu.rabbitmqspring.config

import io.taesu.rabbitmqspring.CommandMessage
import io.taesu.rabbitmqspring.CommandMessageRepository
import io.taesu.rabbitmqspring.OrderAcceptCommand
import io.taesu.rabbitmqspring.OrderCancelCommand
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@Component
class GlobalConsumerMessagePostProcessor(
    private val commandMessageRepository: CommandMessageRepository,
): MessagePostProcessor {
    override fun postProcessMessage(message: Message): Message {
        if (message.isNotDeadLetter) {
            validateMessageSpec(message)
            setTypeId(message)
        }

        return message
    }

    private fun validateMessageSpec(message: Message) {
        try {
            val topic = message.messageProperties.consumerQueue
            val messageId =
                message.messageProperties.messageId ?: throw IllegalArgumentException("AMQP message_id is required.")
            commandMessageRepository.save(
                CommandMessage(
                    topic = topic,
                    notificationId = messageId,
                    payload = message.body.decodeToString()
                )
            )
        } catch (e: Exception) {
            throw AmqpRejectAndDontRequeueException("[postProcessMessage] Message will be rejected", true, e)
        }
    }

    private fun setTypeId(message: Message) {
        val type = message.messageProperties.headers["type"] as? String? ?: return

        val typeId: String = when (type) {
            "accept" -> OrderAcceptCommand::class.java.name
            "cancel" -> OrderCancelCommand::class.java.name
            else -> throw IllegalArgumentException("Unknown type $type")
        }

        message.messageProperties.setHeader("__TypeId__", typeId)
    }
}

val Message.isDeadLetter get() = !this.isNotDeadLetter
val Message.isNotDeadLetter get() = this.messageProperties.xDeathHeader.isNullOrEmpty()
