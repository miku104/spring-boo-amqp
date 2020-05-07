package cn.miku.amqp;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * rabbitmq消息中间件:
 *  自动配置类 RabbitAutoConfiguration
 *  配置属性类 RabbitProperties
 *  使用RabbitTemplate发送消息到队列和获取队列中的消息
 *  @EnableRabbit+@RabbitListener:监听消息队列的内容
 *  rmqpAdmin:RabbitMQ系统管理功能组件
 *      rmqpAdmin:创建和删除 queues exchange binding(绑定规则)
 */
@EnableRabbit//开启基于注解的RabbitMQ模式
@SpringBootApplication
public class SpringBoot02AmqpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot02AmqpApplication.class, args);
    }

}
