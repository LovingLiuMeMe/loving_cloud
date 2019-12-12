package cn.lovingliu.loving.service.impl;

import cn.lovingliu.loving.dao.WelcomeMapper;
import cn.lovingliu.loving.model.Welcome;
import cn.lovingliu.loving.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-11-24
 */
@Service
public class WelcomeServiceImpl implements WelcomeService {
    @Autowired
    private WelcomeMapper welcomeMapper;

    @Override
    public List<Welcome> list() {
        return welcomeMapper.list();
    }

    @Override
    public Integer saveWelcome(Welcome welcome) {
        int count = welcomeMapper.insertSelective(welcome);
        return welcome.getWelcomeId();
    }

    @Override
    public Integer delWelcome(Integer welcomeId) {
        return welcomeMapper.deleteByPrimaryKey(welcomeId);
    }
}
