package com.wjm.bootbook.bean;

import com.wjm.bootbook.entity.dto.review.BaiduTokenDTO;
import com.wjm.bootbook.entity.dto.review.TextReviewDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author stephen wang
 */
@Component
public class BaiduCheckService {

    @Autowired
    RestTemplate restTemplate;

    private String accessToken = "";
    private static final String APP_KEY = "4zbOYeZbhZDzvRG1Gk7OMBKg";
    private static final String SECRET_KEY = "9DviJiem725MhrjOdpSGSPgC9K54sOMq";

    private static final String BASE_URL = "https://aip.baidubce.com/oauth/2.0/token";
    private String accessTokenUrl = null;
    private static final String TEXT_REVIEW_URL =
            "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined?access_token=";
    private final static String JSON_KEY = "text";
    public final static String REVIEW_OK = "合规";

    @PostConstruct
    private void init() {
        this.accessTokenUrl = BASE_URL +
                "?" +
                "client_id=" + APP_KEY +
                "&" +
                "client_secret=" + SECRET_KEY +
                "&" +
                "grant_type=client_credentials";
        BaiduTokenDTO baiduTokenDTO = restTemplate.postForObject(accessTokenUrl, new HashMap<>(), BaiduTokenDTO.class);
        this.accessToken = Objects.requireNonNull(baiduTokenDTO).getAccessToken();
    }

    @Scheduled(cron = "0 0/25 * * * *")
    private void refreshToken() {
        BaiduTokenDTO baiduTokenDTO = restTemplate.postForObject(accessTokenUrl, new HashMap<>(), BaiduTokenDTO.class);
        this.accessToken = Objects.requireNonNull(baiduTokenDTO).getAccessToken();
    }

    public TextReviewDTO textReview(String... text) {
        String url = TEXT_REVIEW_URL + this.accessToken;
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put(JSON_KEY, List.of(text));
        try {
            return restTemplate.postForObject(url, map, TextReviewDTO.class);
        } catch (RestClientException e) {
            return null;
        }
    }
}
