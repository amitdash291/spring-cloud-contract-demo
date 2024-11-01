package com.dash.springcloudcontractdemo.api

import com.dash.springcloudcontractdemo.config.DemoConfigProps
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("v1/orders")
class OrdersController(
    private val demoConfigProps: DemoConfigProps
) {

    @Value("\${demo-config.environment}")
    private lateinit var environment: String

    private final val log = LoggerFactory.getLogger(this.javaClass)

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOrders(): List<OrderDto> {
        log.info("Environment config value using @Value: $environment")
        log.info("Environment config value using @ConfigurationProperties: ${demoConfigProps.environment}")
        return listOf(
            OrderDto(UUID.randomUUID(), 299.59),
            OrderDto(UUID.randomUUID(), 599.99)
        )
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addOrder(
        @RequestBody newOrder: AddOrderDto
    ): ResponseEntity<Any> {
        if (newOrder.value !in (0.0..10_000.0)) return ResponseEntity(object {
            val error = INVALID_ORDER_ERROR_MESSAGE
        }, HttpStatus.BAD_REQUEST)
        return ResponseEntity(OrderDto(UUID.randomUUID(), newOrder.value), HttpStatus.CREATED)
    }
}

const val INVALID_ORDER_ERROR_MESSAGE = "invalid order value, accepted values between: 0.00 - 10,000.00"

class AddOrderDto(
    val value: Double
)

data class OrderDto(
    val id: UUID,
    val value: Double
)
