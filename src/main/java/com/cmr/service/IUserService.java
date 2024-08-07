package com.cmr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmr.dto.LoginFormDTO;
import com.cmr.dto.Result;
import com.cmr.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);

    /**
     * 签到功能
     * @return
     */
    Result sign();

    /**
     * 签到统计
     * @return
     */
    Result signCount();
}
