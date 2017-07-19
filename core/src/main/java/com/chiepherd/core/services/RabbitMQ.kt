package com.chiepherd.core.services

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection
import com.rabbitmq.client.Channel

class RabbitMQ private constructor() {
    val connection : Connection
    val channel    : Channel

    init {
        println("RabbitMQ Singleton /Should be once/")

        val factory = ConnectionFactory()
        factory.host = "192.168.56.200"
        factory.username = "root"
        factory.password = "root"

        connection = factory.newConnection()
        channel    = connection.createChannel()
    }

    fun stop() {
        channel.close()
        connection.close()
    }

    private object Holder { val INSTANCE = RabbitMQ() }

    companion object {
        val instance: RabbitMQ by lazy { Holder.INSTANCE }
    }

    fun sendMessage(exchangeName : String, exchangeType : String,
                     routingKey : String, message : String) {
        channel.exchangeDeclare(exchangeName, exchangeType, true)
        channel.basicPublish(exchangeName, routingKey, null, message.toByteArray())

        println("[x] Sent $routingKey : '$message'")
    }
}
