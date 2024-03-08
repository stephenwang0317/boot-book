package com.wjm.bootbook.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wjm.bootbook.entity.dto.JwtDTO;
import com.wjm.bootbook.entity.pojo.User;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author stephen wang
 */
public class JwtUtils {
    // 30min
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 30L;
    private static final String TOKEN_SECRET = "com.wjm.bootbook";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(TOKEN_SECRET);

    public static String creatJwt(User user) {
        //构建头部信息
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        Date nowDate = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);

        return JWT.create().withHeader(header)
                .withClaim("userId", user.getUserId())//业务信息:员工号
                .withClaim("username", user.getUsername())//业务信息:员工姓名
                .withIssuer("SERVICE")//声明,签名是有谁生成 例如 服务器
                .withNotBefore(new Date())//声明,定义在什么时间之前，该jwt都是不可用的
                .withExpiresAt(expireDate)//声明, 签名过期的时间
                .sign(ALGORITHM);
    }

    private static DecodedJWT getDecodeJwt(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();

        return verifier.verify(token);
    }

    public static JwtDTO getUserFromJwt(String token) throws JWTVerificationException {
        DecodedJWT decodeJwt = getDecodeJwt(token);
        if (!ObjectUtils.isEmpty(decodeJwt)) {
            Claim userId = decodeJwt.getClaim("userId");
            Claim username = decodeJwt.getClaim("username");

            JwtDTO jwtDTO = new JwtDTO();
            jwtDTO.setUserId(userId.asLong());
            jwtDTO.setUsername(username.asString());

            return jwtDTO;
        }

        return null;
    }
}
