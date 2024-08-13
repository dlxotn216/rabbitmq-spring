package io.taesu.rabbitmqspring

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by itaesu on 2024. 8. 14..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
interface CommandMessageRepository: JpaRepository<CommandMessage, Long>
