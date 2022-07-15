package com.zhh.crm.settings.service;

import com.zhh.crm.settings.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 根据用户名密码查询
     * @param map
     * @return User
     */
    User selectByActPwd(Map<String,Object> map);
    /**
     * 查询所有用户
     * @return
     */
    List<User> selectAllUsers();
}
