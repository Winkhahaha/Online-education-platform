package org.mineok.test.rabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/24/ 20:32
 * @Description RabbitMQ入门程序 - 生产者
 */
public class Producer01 {

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
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            // 创建会话通道,生产者和MQ服务所有通信都在channel通道中完成
            channel = connection.createChannel();
            /* 声明队列,如果队列在MQ中则没必要创建
                 参数:
                 String queue,队列名称
                 boolean durable,是否持久化,是则mq重启后队列还在
                 boolean exclusive,是否独占连接(独占则只允许队列在该连接中访问,如果connection连接关闭,队列自动删除(如果此参数true可用于临时队列的创建))
                 boolean autoDelete,队列不再使用是否自动删除此队列(如果此参数true可用于临时队列的创建)
                 Map<String, Object> arguments 队列参数,可以设置一个队列的扩展参数,比如存活时间等
                * */
            channel.queueDeclare(QUEUE, true, false, false, null);
            // 发送消息
            /*
                参数:
                    String exchange,交换机,如果不指定将使用MQ的默认交换机(设置为"")
                    String routingKey,路由key,交换机根据路由key来将消息转发到指定的队列(如果使用默认交换机,则设置为队列名称)
			        BasicProperties props,设置消息的属性
			        byte[] body 消息内容
             */
            channel.basicPublish("", QUEUE, null, "Hello World".getBytes());
            System.out.println("send to MQ --> " + "Hello World");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭通道
            channel.close();
            // 关闭连接
            connection.close();
        }
    }
}
