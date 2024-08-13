package io.taesu.rabbitmqspring

import jakarta.persistence.*
import org.hibernate.annotations.NaturalId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024. 8. 14..
 *
 * @author Lee Tae Su
 * @version rabbitmq-spring
 * @since rabbitmq-spring
 */
@Entity(name = "CommandMessage")
@Table(name = "mq_command_message")
@EntityListeners(value = [AuditingEntityListener::class])
class CommandMessage(
    @Column(name = "topic", nullable = false)
    val topic: String,

    @NaturalId
    @Column(name = "notification_id", nullable = false)
    val notificationId: String,

    @Column(name = "payload", nullable = false, columnDefinition = "text")
    var payload: String,

    @Column(name = "reason", nullable = false, columnDefinition = "text")
    val reason: String = "",

    @Id
    @Column(name = "mq_command_message_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val key: Long = 0L,

    @CreatedDate
    @Column(name = "received_at", nullable = false)
    var receivedAt: LocalDateTime? = null,
)
