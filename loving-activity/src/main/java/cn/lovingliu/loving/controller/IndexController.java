package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.common.ServerResponse;
import cn.lovingliu.loving.enums.CommonCodeEnum;
import cn.lovingliu.loving.model.Carousel;
import cn.lovingliu.loving.model.Welcome;
import cn.lovingliu.loving.service.BannerService;
import cn.lovingliu.loving.service.WelcomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 主页管理
 * @Date：Created in 2019-12-10
 */
@RestController
@Slf4j
public class IndexController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private WelcomeService welcomeService;

    @Value("${ftp.image.prefix}")
    private String imagePrefix;

    @PostMapping("/saveBanner")
    public ServerResponse saveBanner(@RequestBody Carousel carousel) {
        carousel.setCarouselUrl(imagePrefix + carousel.getCarouselUrl());
        Integer carouselId = bannerService.saveBanner(carousel);
        return ServerResponse.createBySuccess("创建成功",carouselId);
    }
    @PostMapping("/delBanner")
    public ServerResponse delBanner(@RequestParam("id") Integer id) {
        int count = bannerService.delBanner(id);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }else {
            return ServerResponse.createByErrorMessage("删除失败");
        }
    }
    @PostMapping("/saveWelcome")
    public ServerResponse saveWelcome(@RequestBody Welcome welcome) {
        welcome.setWelcomeUrl(imagePrefix + welcome.getWelcomeUrl());
        Integer welcomeId = welcomeService.saveWelcome(welcome);
        return ServerResponse.createBySuccess("创建成功",welcomeId);
    }
    @PostMapping("/delWelcome")
    public ServerResponse delWelcome(@RequestParam("id") Integer id) {
        int count = welcomeService.delWelcome(id);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }else {
            return ServerResponse.createByErrorMessage("删除失败");
        }
    }
    @GetMapping("/bannerlist")
    public ServerResponse bannerList() {
        List<Carousel> carouselList = bannerService.findBannerByIsDeleted(CommonCodeEnum.BANNER_STATUS_NO_DELETED.getCode());
        return ServerResponse.createBySuccess(carouselList);
    }

    @GetMapping("/welcomeList")
    public ServerResponse welcomeList() {
        List<Welcome> welcomeList = welcomeService.list();
        return ServerResponse.createBySuccess(welcomeList);
    }
}
