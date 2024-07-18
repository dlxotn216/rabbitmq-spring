package io.taesu.rabbitmqspring

import java.time.LocalDate

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
data class OrderCancelCommand(
    val orderId :String,
    val canceledAt: LocalDate,
    val cancelReason: String
)