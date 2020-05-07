package cn.miku.amqp.service;

import cn.miku.amqp.beans.Book;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author yuyuejin
 * @date 2020/4/21 19:42
 */
@Service
public class RabbitmqService {

    /*@RabbitListener(queues = "miku.emps")
    public void receive(Book book){
        System.out.println("miku.emps队列中的消息"+book);
    }
*/
    @RabbitListener(queues = "miku_news")
    public void receive02(Map map){
        System.out.println(map);
    }

    @RabbitListener(queues = {"joviality_news"})
    public void listenner(Book book){
        System.out.println(book);
    }
}
