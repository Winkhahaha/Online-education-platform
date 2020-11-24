package org.mineok.test.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/24/ 20:32
 * @Description RabbitMQ入门程序 - 消费者
 */
public class Consumer01 {

    // 队列参数设置
    private static final String QUEUE = "hello world";

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
        // 监听队列
        /* 声明队列,如果队列在MQ中则没必要创建
                 参数:
                 String queue,队列名称
                 boolean durable,是否持久化,是则mq重启后队列还在
                 boolean exclusive,是否独占连接(独占则只允许队列在该连接中访问,如果connection连接关闭,队列自动删除(如果此参数true可用于临时队列的创建))
                 boolean autoDelete,队列不再使用是否自动删除此队列(如果此参数true可用于临时队列的创建)
                 Map<String, Object> arguments 队列参数,可以设置一个队列的扩展参数,比如存活时间等
                * */
        channel.queueDeclare(QUEUE, true, false, false, null);
        // 实现消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            /*
            当接收到消息后此方法将被调用
                参数:
                    String consumerTag,消费者标签,用来标识消费者,在监听队列时设置channel.basicConsume
                    Envelope envelope, 通过envelope可以获取
                    AMQP.BasicProperties properties,消息属性(如果有设置)
                    byte[] body 消息内容
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 获取交换机
                String exchange = envelope.getExchange();
                // 消息id,MQ在通道中表示消费的id,可用于确认消息已接收
                long deliveryTag = envelope.getDeliveryTag();
                // 消息内容
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Consumer receive message -->" + message);
            }
        };
        /*
        监听队列
            参数:
            String queue,队列名称
            boolean autoAck,自动回复,当消费者接收到消息后告诉MQ消息接收,如果将此参数设置为true表示会自动回复,false则要通过编程回复
            Consumer callback 消费方法,当消费者接收到消息要执行的方法
         */
        channel.basicConsume(QUEUE, true, defaultConsumer);
    }
}
