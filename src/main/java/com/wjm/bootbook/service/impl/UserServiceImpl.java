package com.wjm.bootbook.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjm.bootbook.entity.dto.LoginDTO;
import com.wjm.bootbook.entity.dto.RegisterDTO;
import com.wjm.bootbook.entity.pojo.User;
import com.wjm.bootbook.mapper.UserMapper;
import com.wjm.bootbook.service.UserService;
import com.wjm.bootbook.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author stephen wang
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User saveUser(String username, String password) {
        String salt = Md5Utils.getSalt();
        String encrypted = Md5Utils.encrypt(password, salt);
        Long curTime = System.currentTimeMillis();

        User user = new User(null, username, encrypted, salt, null, curTime);
        int insert = userMapper.insert(user);

        return insert > 0 ? user : null;
    }

    @Override
    public User userLogin(Long userId, String password) {

        User userById = userMapper.selectById(userId);
        if (userById == null) {
            return null;
        }

        String salt = userById.getSalt();
        String encrypted = Md5Utils.encrypt(password, salt);
        if (encrypted.equals(userById.getPassword())) {
            return userById;
        } else {
            return null;
        }
    }

    @Override
    public User updateUserById(Long userId, String username, String password) {
        User user = userMapper.selectById(userId);

        if (!ObjectUtils.isEmpty(password)) {
            String salt = user.getSalt();
            String encrypted = Md5Utils.encrypt(password, salt);
            user.setPassword(encrypted);
        }
        if (!ObjectUtils.isEmpty(username)) {
            user.setUsername(username);
        }
        int i = userMapper.updateById(user);
        return i > 0 ? user : null;
    }
}