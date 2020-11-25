package org.mineok.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/25/ 15:27
 * @Description
 */
@SpringBootApplication
public class ConsumerMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerMQApplication.class, args);
    }
}
