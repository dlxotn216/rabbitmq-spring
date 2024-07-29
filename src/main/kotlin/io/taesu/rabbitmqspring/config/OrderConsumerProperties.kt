package io.taesu.rabbitmqspring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * Created by itaesu on 2024. 7. 17..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@ConfigurationProperties("app.orders.consumer")
class OrderConsumerProperties @ConstructorBinding constructor(
    val exchange: String,
    val instances: Map<String, ConsumerProperty>,
) {
    val instanceValues get() = instances.values
}
