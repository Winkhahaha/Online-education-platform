package org.mineok.test.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/24/ 22:22
 * @Description RabbitMQ入门程序 - 消费者(发布订阅模式)_EMAIL
 */
public class Consumer04_topics_email {

    // 队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    // 交换机名称
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    // 路由key名称
    private static final String TOPICS_ROUTING_EMAIL = "inform.#.email.#";  // inform.email.XXX

    public static void main(String[] args) throws IOException, TimeoutException {
        // 通过连接工厂和MQ建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("112.126.78.213");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setUsername("guest");
        // 设置虚拟机,一个MQ的服务可以设置多个虚拟机,每个虚拟机相当于一个独立的MQ
        connectionFactory.setVirtualHost("/");
        // 建立新连接
        Connection connection = null;
        connection = connectionFactory.newConnection();
        // 创建会话通道,消费者和MQ服务所有通信都在channel通道中完成
        Channel channel = connection.createChannel();
        /* 声明队列*/
        channel.queueDeclare(QUEUE_INFORM_EMAIL, true, false, false, null);
        /* 设置一个交换机*/
        channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
        /* 队列和交换机进行绑定*/
        channel.queueBind(QUEUE_INFORM_EMAIL,    EXCHANGE_TOPICS_INFORM, TOPICS_ROUTING_EMAIL);

        // 实现消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            /*当接收到消息后此方法将被调用*/
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 获取交换机
                String exchange = envelope.getExchange();
                // 消息id,MQ在通道中表示消费的id,可用于确认消息已接收
                long deliveryTag = envelope.getDeliveryTag();
                // 消息内容
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Consumer_topics_email receive message -->" + message);
            }
        };
        /*监听队列*/
        channel.basicConsume(QUEUE_INFORM_EMAIL, true, defaultConsumer);
    }
}
