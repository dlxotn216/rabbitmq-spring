package io.taesu.rabbitmqspring.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

/**
 * Created by itaesu on 2024. 7. 16..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@Configuration
class RabbitMqConfig {
    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        objectMapper: ObjectMapper,
    ): RabbitTemplate {
        return RabbitTemplate(connectionFactory).apply {
            this.messageConverter = Jackson2JsonMessageConverter(objectMapper)
            this.setRetryTemplate(
                RetryTemplate().apply {
                    this.setBackOffPolicy(
                        ExponentialBackOffPolicy().apply {
                            this.maxInterval = 20000    // 최대 대기 시간
                            this.initialInterval = 500  // 최초 대기 시간
                            this.multiplier = 2.0       // 재시도 시 대기시간 증폭률
                        }
                    )
                    this.setRetryPolicy(SimpleRetryPolicy(5))   // 최대 재시도 횟수
                }
            )
        }
    }
}
