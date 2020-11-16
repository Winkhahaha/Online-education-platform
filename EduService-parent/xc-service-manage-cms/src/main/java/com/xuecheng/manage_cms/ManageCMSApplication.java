package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author Gaoming
 * @Email mineok@foxmail.com
 * @Date 2020/11/16/ 21:44
 * @Description
 */
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")// 扫描实体类
@ComponentScan(basePackages = {"com.xuecheng.api"})// 扫描接口
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"})// 扫描本项目下的所有类
public class ManageCMSApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCMSApplication.class, args);
    }
}
