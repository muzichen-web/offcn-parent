package com.offcn.user.service;

import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;
import org.springframework.stereotype.Component;

import java.util.List;

public interface UserService {

    //用户注册
    public void registerUser(TMember member);

    //用户登录
    public TMember login(String username, String password);

    //根据用户id，获取用户信息
    public TMember findTmemberById(Integer id);

    /**
     * 获取用户收货地址
     * @param memberId
     * @return
     */
    List<TMemberAddress> addressList(Integer memberId);

}
