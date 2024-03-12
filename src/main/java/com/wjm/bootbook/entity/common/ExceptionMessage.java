package com.wjm.bootbook.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author stephen wang
 */

@AllArgsConstructor
@Getter
public enum ExceptionMessage {

    LOGIN_ERROR("Id or Password Error"),
    NOT_LOGIN("Not Login"),
    GET_USER_ERROR("get user info error"),
    UPDATE_USER_ERROR("Update User Error"),
    JWT_VERIFICATION_ERROR("Jwt Verification Error"),
    ADD_ARTICLE_ERROR("Add Article Error"),
    GET_ARTICLE_ERROR("get Article Error"),
    DELETE_ARTICLE_ERROR("Delete Article Error"),
    UPDATE_ARTICLE_ERROR("Update Article Error"),
    NO_SUCH_ARTICLE("no such article"),
    CANT_UPDATE_ARTICLE("you cannot update others' article"),
    ADD_COMMENT_ERROR("Add Comment Error"),
    DELETE_COMMENT_ERROR("Delete Comment Error"),
    UPDATE_COMMENT_ERROR("Update Comment Error"),
    GET_COMMENT_ERROR("Get Comment Error"),
    SERVER_TOO_BUSY("server too busy");


    private final String message;
}
