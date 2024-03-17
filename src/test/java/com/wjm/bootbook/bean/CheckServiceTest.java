package com.wjm.bootbook.bean;

import com.wjm.bootbook.entity.dto.review.BaiduTokenDTO;
import com.wjm.bootbook.entity.dto.review.TextReviewDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CheckServiceTest {

    @Test
    void test01() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(factory);

        String APP_KEY = "4zbOYeZbhZDzvRG1Gk7OMBKg";
        String SECRET_KEY = "9DviJiem725MhrjOdpSGSPgC9K54sOMq";
        String BASE_URL = "https://aip.baidubce.com/oauth/2.0/token";

        String url = BASE_URL +
                "?" +
                "client_id=" + APP_KEY +
                "&" +
                "client_secret=" + SECRET_KEY +
                "&" +
                "grant_type=client_credentials";
        BaiduTokenDTO baiduTokenDTO = restTemplate.postForObject(url, new HashMap<>(), BaiduTokenDTO.class);
        String accessToken = baiduTokenDTO.getAccessToken();
        System.out.println(accessToken);
    }

    @Test
    public void textReview01() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(factory);
        String accessToken = "24.dcf67e2b55be5519881370ddad9a4b84.2592000.1712979135.282335-56360618";


        String TEXT_REVIEW_URL =
                "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?access_token="
                        + accessToken;
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        //HashMap<String, String> hashMap = new HashMap<>();
        map.put("text", List.of("今天天气真好"));
        Object o = restTemplate.postForObject(TEXT_REVIEW_URL, map, Object.class);
        System.out.println(o);
    }
}
