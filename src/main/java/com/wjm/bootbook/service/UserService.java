package com.wjm.bootbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjm.bootbook.entity.dto.LoginDTO;
import com.wjm.bootbook.entity.dto.RegisterDTO;
import com.wjm.bootbook.entity.pojo.User;

import java.util.List;

/**
 * @author stephen wang
 */
public interface UserService extends IService<User> {
    User saveUser(String username, String password);

    User userLogin(Long userId, String password);

    User updateUserById(Long userId, String username, String password);
}