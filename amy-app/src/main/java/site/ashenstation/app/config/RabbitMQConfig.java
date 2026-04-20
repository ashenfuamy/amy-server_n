package site.ashenstation.app.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 1. 定义队列名称
    public static final String UPLOAD_TASK_QUEUE = "amy_video_upload_queue";
    // 2. 定义交换机名称
    public static final String UPLOAD_TASK_EXCHANGE = "amy_video_upload_exchange";
    // 3. 定义路由key
    public static final String ROUTING_KEY = "amy_routing_key";

    // ====================== 声明队列 ======================
    @Bean
    public Queue testQueue() {
        // durable: 持久化队列（重启不丢失）
        return QueueBuilder.durable(UPLOAD_TASK_QUEUE).build();
    }

    // ====================== 声明交换机（Direct直连模式） ======================
    @Bean
    public DirectExchange testExchange() {
        return ExchangeBuilder.directExchange(UPLOAD_TASK_EXCHANGE).durable(true).build();
    }

    // ====================== 队列绑定交换机 ======================
    @Bean
    public Binding testBinding() {
        return BindingBuilder.bind(testQueue())
                .to(testExchange())
                .with(ROUTING_KEY);
    }
}
