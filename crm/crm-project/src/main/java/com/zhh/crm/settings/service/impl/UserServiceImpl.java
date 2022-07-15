package com.zhh.crm.settings.service.impl;

import com.zhh.crm.settings.bean.User;
import com.zhh.crm.settings.mapper.UserMapper;
import com.zhh.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 返回一个User
     * @param map
     * @return
     */
    @Override
    public User selectByActPwd(Map<String, Object> map) {
        return userMapper.selectByActPwd(map);
    }

    @Override
    public List<User> selectAllUsers() {
        return userMapper.selectAllUsers();
    }
}
