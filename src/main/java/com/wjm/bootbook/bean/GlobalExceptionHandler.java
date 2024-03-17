package com.wjm.bootbook.bean;

import com.wjm.bootbook.entity.common.ResponseResult;

import com.wjm.bootbook.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author stephen wang
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    ResponseResult<String> handleException(CustomException e) {
        log.warn(e.getMessage());
        return ResponseResult.fail(e.getMessage());
    }
}
