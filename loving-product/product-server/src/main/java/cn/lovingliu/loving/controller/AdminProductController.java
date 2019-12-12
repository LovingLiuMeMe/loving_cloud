package cn.lovingliu.loving.controller;

import cn.lovingliu.loving.commmon.CommonPage;
import cn.lovingliu.loving.commmon.ServerResponse;
import cn.lovingliu.loving.dto.GoodsInfoDTO;
import cn.lovingliu.loving.enums.CommonCodeEnum;
import cn.lovingliu.loving.model.GoodsInfo;
import cn.lovingliu.loving.service.FileService;
import cn.lovingliu.loving.service.GoodsInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-10
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminProductController {

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private FileService fileService;

    @Value("${ftp.image.prefix}")
    private String imagePrefix;

    @GetMapping("/list")
    public ServerResponse<CommonPage<GoodsInfo>> list(
            @RequestParam(value = "pageNum",defaultValue = "1")
                    Integer pageNum,

            @RequestParam(value = "pageSize",defaultValue = "10")
                    Integer pageSize,

            @RequestParam(value = "orderBy",defaultValue = "create_time")
                    String orderBy,

            @RequestParam(value = "orderType",defaultValue = "desc")
                    String orderType,

            @RequestParam(value = "search",required = false)
                    String keyword){
        List<GoodsInfo> goodsInfoList = goodsInfoService.listByAdmin(pageNum,pageSize,orderBy,orderType,keyword);
        return ServerResponse.createBySuccess("获取成功", CommonPage.restPage(goodsInfoList));
    }

    @PostMapping("/updateSellStatus")
    /**
     * @Desc 注意@RequestParam 要用于接收前端发送的信息 必须使用qs.stringifty(params)
     * @Author LovingLiu
     */
    public ServerResponse updateSellStatus(@RequestParam("GoodsInfoId") Long GoodsInfoId){
        int count = goodsInfoService.updateGoodsInfoSellStatus(GoodsInfoId);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }else{
            return ServerResponse.createByErrorMessage("更新失败");
        }
    }

    @PostMapping("/save")
    public ServerResponse save(@Valid @RequestBody GoodsInfoDTO goodsInfoDTO, BindingResult result){
        if(result.hasErrors()){
            return ServerResponse.createByErrorMessage(result.getFieldError().getDefaultMessage());
        }
        int count = goodsInfoService.saveGoodsInfo(goodsInfoDTO);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("操作成功");
        }
        return ServerResponse.createByErrorMessage("操作失败");
    }

    @GetMapping("/info")
    public ServerResponse info(@RequestParam("goodsInfoId") Long goodsInfoId){
        return ServerResponse.createBySuccess("获取成功",goodsInfoService.findByGoodsId(goodsInfoId));
    }

    @PostMapping("/delete")
    public ServerResponse delete(@RequestParam("goodsInfoId") Long goodsInfoId){
        List<Long> goodsInfoIdList = Lists.newArrayList(goodsInfoId);

        int count = goodsInfoService.removeGoodsInfo(goodsInfoIdList);
        log.info("删除商品的数量: {}",count);
        if(count > 0){
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    @GetMapping("/listAll")
    public ServerResponse listAll(){
        List<GoodsInfo> goodsInfoList = goodsInfoService.listAllBySellStatus(CommonCodeEnum.PRODUCT_STATUS_ON_SELL.getCode());
        return ServerResponse.createBySuccess("查询成功",goodsInfoList);
    }
    @PostMapping("/imageUpload")
    public ServerResponse imageUpload(MultipartFile uploadFile, HttpServletRequest request){
        String path = request.getSession().getServletContext().getRealPath("upload");// 拿到的是和WEB-INF同级的（服务器的发布环境）
        String targetFileName = fileService.upload(uploadFile,path);
        String imageUrl = imagePrefix+targetFileName;
        Map<String,Object> map = new HashMap<>();
        map.put("url",imageUrl);
        return ServerResponse.createBySuccess("上传成功", map);
    }

    @PostMapping("/imageDelete")
    /**
     * @Desc 图片的上传 注意 前后端数组数据的交互 https://blog.csdn.net/pifutan/article/details/86320705
     * @Author LovingLiu
     */
    public ServerResponse imageDelete(@RequestParam String[] fileNames){
        Boolean is_delete = fileService.delete(fileNames);
        if(is_delete){
            return ServerResponse.createBySuccess("删除成功");
        }
        return ServerResponse.createBySuccess("删除失败");
    }
}
