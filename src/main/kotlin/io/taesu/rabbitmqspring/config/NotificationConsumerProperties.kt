package io.taesu.rabbitmqspring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@ConfigurationProperties("app.notification.consumer")
class NotificationConsumerProperties @ConstructorBinding constructor(
    val exchange: String,
    val instances: List<ConsumerProperty>,
)
