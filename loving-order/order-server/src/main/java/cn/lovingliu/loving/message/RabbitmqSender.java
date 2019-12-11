package cn.lovingliu.loving.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-02
 */
//启动这个绑定
@EnableBinding(Barista.class)
@Service //注入到spring容器
public class RabbitmqSender {
    //注入Barista
    @Autowired
    private Barista barista;

    // 发送消息
    public Boolean sendMessage(String message){
        boolean sendStatus = barista.orderOutputChannel().send(MessageBuilder.withPayload(message).build());
        System.err.println("--------------sending -------------------");
        System.out.println("发送数据：" + message + ",sendStatus: " + sendStatus);
        return sendStatus;
    }
}
