package cn.lovingliu.loving.message;

import cn.lovingliu.loving.product.common.DecreaseStockInput;
import cn.lovingliu.loving.product.common.GsonUtil;
import cn.lovingliu.loving.service.GoodsInfoService;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-02
 */
@EnableBinding(Barista.class)
@Service
@Slf4j
public class RabbitmqReceiver {

    @Autowired
    private GoodsInfoService goodsInfoService;

    @StreamListener(Barista.INPUT_CHANNEL)
    @SendTo(Barista.OUTPUT_SENDTO_CHANNEL)
    public String receiver(String message){
        log.info("【product】message=>{}",message);
        String delMsg = String.format("【product】message=>%s",message);
        /**
         * 业务逻辑处理 减库存
         */
        List<DecreaseStockInput> decreaseStockInputList = (List<DecreaseStockInput>)GsonUtil.fromJson(message, new TypeToken<List<DecreaseStockInput>>(){}.getType());
        goodsInfoService.decreaseStock(decreaseStockInputList);
        return delMsg;
    }

    @StreamListener(Barista.INPUT_SENDTO_CHANNEL)
    public void sendToReceiver(String message){
        log.info("处理之后的信息=>{}",message);
    }
}
