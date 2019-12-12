package cn.lovingliu.loving.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author：LovingLiu
 * @Description: 可以看出来他们是敲好相同的
 * @Date：Created in 2019-12-02
 */
public interface Barista {
    String INPUT_CHANNEL = "input_channel";
    @Input(Barista.INPUT_CHANNEL)
    SubscribableChannel orderInputChannel();

    String INPUT_SENDTO_CHANNEL = "input_sendto_channel";
    @Input(Barista.INPUT_SENDTO_CHANNEL)
    SubscribableChannel sendToInputChannel();

    String OUTPUT_SENDTO_CHANNEL = "output_sendto_channel";
    @Output(Barista.OUTPUT_SENDTO_CHANNEL)
    MessageChannel sendToOutputChannel();
}
