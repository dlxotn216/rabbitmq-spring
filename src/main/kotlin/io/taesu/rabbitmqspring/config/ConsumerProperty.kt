package io.taesu.rabbitmqspring.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
class ConsumerProperty(
    val routingKey: String,
    val topic: String,
    val dlRoutingKey: String?,
    val dlTopic: String?,
)
