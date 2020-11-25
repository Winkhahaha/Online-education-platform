package org.mineok.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import org.mineok.rabbitmq.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/25/ 16:06
 * @Description 接收生产者消息
 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_EMAIL})
    public void receive_email(String s, Message message, Channel channel) {
        System.out.println("receive message from MQ --> " + s);
    }
}
