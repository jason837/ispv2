package icu.youcompleteme.ispv2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import icu.youcompleteme.ispv2.entity.IspUser;

import java.util.List;

/**
 * 登录用户(IspUser)表服务接口
 *
 * @author makejava
 * @since 2022-08-11 16:24:27
 */
public interface IspUserService extends IService<IspUser> {

    List<IspUser> queryAll();

    void add();

    void updateUser();

    void del();
}

