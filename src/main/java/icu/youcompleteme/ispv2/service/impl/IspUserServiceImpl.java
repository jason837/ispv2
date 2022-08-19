package icu.youcompleteme.ispv2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.youcompleteme.ispv2.entity.IspUser;
import icu.youcompleteme.ispv2.mapper.IspUserMapper;
import icu.youcompleteme.ispv2.service.IspUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 登录用户(IspUser)表服务实现类
 *
 * @author makejava
 * @since 2022-08-11 16:24:27
 */
@Service
public class IspUserServiceImpl extends ServiceImpl<IspUserMapper, IspUser> implements IspUserService {

    @Override
    public List<IspUser> queryAll() {
        LambdaQueryWrapper<IspUser> wrapper = new LambdaQueryWrapper<>();

        return baseMapper.selectList(wrapper);
    }

    @Override
    public void add() {

    }

    @Override
    public void updateUser() {

    }

    @Override
    public void del() {

    }
}

