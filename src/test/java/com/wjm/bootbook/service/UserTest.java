package com.wjm.bootbook.service;

import com.wjm.bootbook.entity.dto.RegisterDTO;
import com.wjm.bootbook.entity.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    UserService userService;


    @Test
    void testUserInsert() {
        RegisterDTO registerDTO = new RegisterDTO("admin", "admin");
        boolean b = userService.saveUser(registerDTO);
        System.out.println(b);
    }

    @Test
    void testSelectOne(){
        User userById = userService.getUserById(100000L);
        System.out.println(userById);
    }
}
