package com.wjm.bootbook.controller;

import com.wjm.bootbook.entity.common.ExceptionMessage;
import com.wjm.bootbook.entity.common.ResponseResult;
import com.wjm.bootbook.entity.dto.JwtDTO;
import com.wjm.bootbook.entity.dto.LoginDTO;
import com.wjm.bootbook.entity.dto.RegisterDTO;
import com.wjm.bootbook.entity.dto.UserUpdateDTO;
import com.wjm.bootbook.entity.pojo.User;
import com.wjm.bootbook.entity.vo.LoginVO;
import com.wjm.bootbook.entity.vo.RegisterVO;
import com.wjm.bootbook.entity.vo.UserUpdateVO;
import com.wjm.bootbook.exception.CustomException;
import com.wjm.bootbook.exception.RegisterException;
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
            throw new RegisterException("register failed, because: " + failReason);
        }

        String username = dto.getUsername();
        String password = dto.getPassword();
        User user = userService.saveUser(username, password);
        if (user == null) {
            throw new RegisterException("register failed");
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
            throw new CustomException(failReason);
        }

        Long userId = dto.getUserId();
        String password = dto.getPassword();
        User user = userService.userLogin(userId, password);
        if (user == null) {
            throw new CustomException(ExceptionMessage.LOGIN_ERROR.getMessage());
        } else {
            String s = JwtUtils.creatJwt(user);
            return ResponseResult.success(new LoginVO(s));
        }
    }

    @GetMapping("/test")
    JwtDTO test01(JwtDTO dto) {
        return dto;
    }

    @GetMapping("/info/{userId}")
    ResponseResult<User> getUserInfo(@PathVariable Long userId) {
        User user = userService.getById(userId);
        user.setSalt(null);
        user.setPassword(null);
        if (ObjectUtils.isEmpty(user)) {
            throw new CustomException(ExceptionMessage.GET_USER_ERROR.getMessage());
        }
        return ResponseResult.success(user);
    }

    @PutMapping("/update")
    ResponseResult<UserUpdateVO> updateUserInfo(@RequestBody UserUpdateDTO dto, JwtDTO jwt) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        Long userId = jwt.getUserId();
        User user = userService.updateUserById(userId, username, password);
        if (ObjectUtils.isEmpty(user)) {
            throw new CustomException(ExceptionMessage.UPDATE_USER_ERROR.getMessage());
        }
        UserUpdateVO userUpdateVO = new UserUpdateVO(user.getUsername(), user.getAvatar());
        return ResponseResult.success(userUpdateVO);
    }
}
