package fr.raven.messaging.rabbitmq.exception

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.AlreadyClosedException
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ExceptionHandler
import com.rabbitmq.client.impl.StrictExceptionHandler
import fr.raven.log.LoggerInterface
import java.io.IOException
import java.util.concurrent.TimeoutException

class ExceptionHandler(private val logger: LoggerInterface) : StrictExceptionHandler(), ExceptionHandler {
    override fun handleChannelKiller(channel: Channel, exception: Throwable, logMessage: String, closeMessage: String) {
        logger.error("$logMessage threw an exception for channel $channel: ${exception.message}")
        exception.printStackTrace()

        try {
            channel.close(AMQP.REPLY_SUCCESS, "Closed due to exception from $closeMessage")
        } catch (ace: AlreadyClosedException) {
            // noop
        } catch (ace: TimeoutException) {
            // noop
        } catch (ioe: IOException) {
            logger.error("Failure during close of channel $channel after $exception: ${ioe.message}")
            channel.connection.abort(AMQP.INTERNAL_ERROR, "Internal error closing channel for $closeMessage")
            ioe.printStackTrace()
        }
    }
}
