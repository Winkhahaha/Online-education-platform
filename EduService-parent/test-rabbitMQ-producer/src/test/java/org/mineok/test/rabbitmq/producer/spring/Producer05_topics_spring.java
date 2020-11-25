package org.mineok.test.rabbitmq.producer.spring;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mineok.rabbitmq.ProducerMQApplication;
import org.mineok.rabbitmq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/25/ 14:06
 * @Description RabbitMQ - SpringBoot整合
 */
@SpringBootTest(classes = ProducerMQApplication.class)
@RunWith(SpringRunner.class)
public class Producer05_topics_spring {

    // 使用rabbitTemplate发送消息
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Test
    public void testSendEmail() {
        /*
       参数:
         String exchange,交换机名称
         String routingKey,routingKey名称
         Object message,消息内容
         CorrelationData correlationData
    * */
        String message = "send email to MQ by SpringBoot";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPICS_INFORM, "inform.email", message);
    }

}
