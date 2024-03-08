package com.wjm.bootbook.controller;

import com.wjm.bootbook.entity.common.ResponseResult;
import com.wjm.bootbook.entity.dto.JwtDTO;
import com.wjm.bootbook.entity.dto.LoginDTO;
import com.wjm.bootbook.entity.dto.RegisterDTO;
import com.wjm.bootbook.entity.dto.UserUpdateDTO;
import com.wjm.bootbook.entity.pojo.User;
import com.wjm.bootbook.entity.vo.LoginVO;
import com.wjm.bootbook.entity.vo.RegisterVO;
import com.wjm.bootbook.entity.vo.UserUpdateVO;
import com.wjm.bootbook.service.UserService;
import com.wjm.bootbook.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author stephen wang
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    ResponseResult<RegisterVO> userRegister(
            @Validated @RequestBody RegisterDTO dto,
            BindingResult bindingResult) {
        // param check failed
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder sb = new StringBuilder();
            errors.forEach(objectError -> sb.append(objectError.getDefaultMessage()).append("; "));
            String failReason = sb.toString();
            RegisterVO vo = new RegisterVO(false, null);
            return ResponseResult.fail(vo, failReason);
        }

        String username = dto.getUsername();
        String password = dto.getPassword();
        User user = userService.saveUser(username, password);
        if (user == null) {
            RegisterVO vo = new RegisterVO(false, null);
            return ResponseResult.fail(vo, "register fail");
        } else {
            RegisterVO vo = new RegisterVO(true, user.getUserId());
            return ResponseResult.success(vo);
        }
    }

    @PostMapping("/login")
    ResponseResult<LoginVO> userLogin(
            @RequestBody @Validated LoginDTO dto,
            BindingResult bindingResult) {
        // param check failed
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder sb = new StringBuilder();
            errors.forEach(objectError -> {
                sb.append(objectError.getDefaultMessage()).append("; ");
            });
            String failReason = sb.toString();
            LoginVO vo = new LoginVO();
            return ResponseResult.fail(vo, failReason);
        }

        Long userId = dto.getUserId();
        String password = dto.getPassword();
        User user = userService.userLogin(userId, password);
        if (user == null) {
            return ResponseResult.fail(new LoginVO(), "id or password error");
        } else {
            String s = JwtUtils.creatJwt(user);
            return ResponseResult.success(new LoginVO(s));
        }
    }

    @GetMapping("/test")
    JwtDTO test01(JwtDTO dto) {
        return dto;
    }

    @PostMapping("/info/{userId}")
    ResponseResult<User> getUserInfo(@PathVariable Long userId) {
        User user = userService.getById(userId);
        user.setSalt(null);
        user.setPassword(null);
        if (ObjectUtils.isEmpty(user)) {
            return ResponseResult.fail(null, "get user info error");
        }
        return ResponseResult.success(user);
    }

    @PostMapping("/update")
    ResponseResult<UserUpdateVO> updateUserInfo(@RequestBody UserUpdateDTO dto, JwtDTO jwt) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        Long userId = jwt.getUserId();
        User user = userService.updateUserById(userId, username, password);
        if (ObjectUtils.isEmpty(user)) {
            return ResponseResult.fail(null, "update user info failed");
        }
        UserUpdateVO userUpdateVO = new UserUpdateVO(user.getUsername(), user.getAvatar());
        return ResponseResult.success(userUpdateVO);
    }
}
