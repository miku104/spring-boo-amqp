package cn.miku.amqp;

import cn.miku.amqp.beans.Book;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringBoot02AmqpApplicationTests {

    /**
     * rabbitmq系统的操作模板
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 管理rabbitmq系统的功能组件
     */
    @Autowired
    private AmqpAdmin amqpAdmin;

    //发布订阅模式,此交换机的type为fanout类型
    @Test
    public void fanoutSend(){
        //需要自己构建消息体和消息头
//        rabbitTemplate.send(exchange,routekey,message);

        Map<String,Object> map=new HashMap<>();
        map.put("msg","helloworld");
        map.put("miku","miku");
        map.put("时崎","狂三");
        //默认是实现了SimpleMessageConverter对象,该类是使用了jdk提供的SerializationUtils序列化工具类实现的序列化
        //会自动序列化,默认使用jdk的序列化方式      交换器                         队列      消息
        rabbitTemplate.convertAndSend("exchange.fanout","",map);
    }

    //路由模式,此交换机的type为direct类型
    @Test
    void contextLoads() {
        /**
         * 1.object会默认当做消息体,只需要传入对象即可,自动序列化发送给rabbitmq消息中间件
         * 2.需要实现自动序列化成json串,添加自动以的MessageConverter组件,给组件是Jackson2JsonMessageConverter类型的,就会
         * 将消息自动序列化为json
         */
        //将Javabean对象序列化发送到队列中
        rabbitTemplate.convertAndSend("exchange.direct","joviality_news",new Book("西游记","吴承恩"));
    }


    @Test
    public void fun1() {
        /**
         * 读取队列消息
         *将队列中有未读取的消息返回
         * Failed to convert Message content异常说明:javabean没有实现无参构造
         */
        Object o = rabbitTemplate.receiveAndConvert("miku_news");
        System.out.println(o.getClass());
        System.out.println(o);
    }

    //广播(发布订阅式)
    @Test
    public void fanout(){
        rabbitTemplate.convertAndSend("exchange.fanout","mi",new Book("三国演义","罗贯中"));
    }

    @Test
    public void fanoutMessage(){
        Object o = rabbitTemplate.receiveAndConvert("miku.news");
        System.out.println(o.getClass());
        System.out.println(o);
    }

    /**
     * 发布订阅模式:
     *exchange.fanout 的type为fanout
     * 不处理路由键
     */
    @Test
    public void testAmqpAdmin_fanout(){
        //创建exchange
        amqpAdmin.declareExchange(new FanoutExchange("exchange.fanout",true,true));

        //创建queues,队列
        amqpAdmin.declareQueue(new Queue("miku_news"));

        //将exchange交换器与消息队列进行绑定,从而可以通过交换器发送到已绑定的队列中,同一个消息被对个队列所读取
        amqpAdmin.declareBinding(new Binding("miku_news", Binding.DestinationType.QUEUE,"exchange.fanout","",null));
    }

    /**
     * 路由模式:
     *  exchange.joviality 的类型为direct
     *  根据路由键来过滤发布到队列中
     */
    @Test
    public void testAmqpAdmin_direct(){
        //创建exchange
        amqpAdmin.declareExchange(new DirectExchange("exchange.direct"));

        //创建queues,队列
        amqpAdmin.declareQueue(new Queue("joviality_news"));

        //将exchange交换器与消息队列进行绑定,从而可以通过交换器根据发送的路由键发送消息到某个符合条件的消息队列
        amqpAdmin.declareBinding(new Binding("joviality_news", Binding.DestinationType.QUEUE,"exchange.direct","joviality_news",null));
    }

    /**
     * 主题模式:
     * 路由键通配符与某一模式进行组合
     */
    @Test
    public void testAmqp_topic(){
        //创建交换机
        amqpAdmin.declareExchange(new TopicExchange("exchange.topic"));

        //创建队列
        amqpAdmin.declareQueue(new Queue("cheer_news"));

        //绑定队列
        amqpAdmin.declareBinding(new Binding("cheer_news",Binding.DestinationType.QUEUE,"exchange.topic","cheer.#",null));
    }

    @Test
    public void t(){
//        rabbitTemplate.convertAndSend("cheer.joviality","cheer.*",new Book("水浒传","施耐庵"));
        Object o = rabbitTemplate.receiveAndConvert("cheer.news");
        System.out.println(o);
    }

}
