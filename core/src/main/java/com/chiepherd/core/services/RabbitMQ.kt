package com.chiepherd.core.services

import com.rabbitmq.client.*
import com.rabbitmq.tools.jsonrpc.JsonRpcServer.response
import java.io.IOException
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.UUID





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

    @Throws(IOException::class, InterruptedException::class)
    fun sendMessage(routingKey : String, message : String): String {
        val replyQueueName = channel.queueDeclare().queue

        val corrId = UUID.randomUUID().toString()

        val props = AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build()

        channel.basicPublish("chiepherd.main", routingKey, props, message.toByteArray(charset("UTF-8")))
        println("[${routingKey}] -> ${message}")

        val response = ArrayBlockingQueue<String>(1)

        channel.basicConsume(replyQueueName, true, object : DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
                if (properties.correlationId == corrId) {
                    response.offer(String(body))
                }
            }
        })

        val res = response.take()
        println("[${routingKey}] <- ${res}")
        return res
    }
}
