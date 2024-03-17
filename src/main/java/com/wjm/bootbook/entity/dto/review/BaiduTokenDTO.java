package com.wjm.bootbook.entity.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author stephen wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiduTokenDTO {
    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("session_key")
    private String sessionKey;

    @JsonProperty("access_token")
    private String accessToken;

    private String scope;

    @JsonProperty("session_secret")
    private String sessionSecret;
}
