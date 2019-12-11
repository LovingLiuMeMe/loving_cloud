package cn.lovingliu.loving.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-02
 */
public interface Barista {
    String OUTPUT_CHANNEL = "output_channel";
    @Output(Barista.OUTPUT_CHANNEL)
    MessageChannel orderOutputChannel();
}
