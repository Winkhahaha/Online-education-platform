package org.mineok.rabbitmq.config;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/25/ 15:32
 * @Description RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {

    /* 采用TOPICS的工作模式*/
    // 队列名称
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    // 交换机名称
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    // 路由key名称
    public static final String TOPICS_ROUTING_EMAIL = "inform.#.email.#";  // inform.email.XXX
    public static final String TOPICS_ROUTING_SMS = "inform.#.sms.#";

    // 声明交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        // durable(true) 交换机持久化,mq重启后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    // 声明email队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL() {
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    // 声明sms队列
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS() {
        return new Queue(QUEUE_INFORM_SMS);
    }

    // 队列绑定交换机
    // 绑定email队列
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL) Queue queueEmail,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queueEmail).to(exchange).with(TOPICS_ROUTING_EMAIL).noargs();
    }

    // 绑定sms队列
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue queueSMS,
                                            @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queueSMS).to(exchange).with(TOPICS_ROUTING_SMS).noargs();
    }
}
