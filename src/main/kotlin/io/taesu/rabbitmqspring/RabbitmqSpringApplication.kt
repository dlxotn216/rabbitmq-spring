package io.taesu.rabbitmqspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class RabbitmqSpringApplication

fun main(args: Array<String>) {
    runApplication<RabbitmqSpringApplication>(*args)
}
