package com.wjm.bootbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjm.bootbook.annotation.RedisCache;
import com.wjm.bootbook.bean.BaiduCheckService;
import com.wjm.bootbook.entity.common.ExceptionMessage;
import com.wjm.bootbook.entity.common.ResponseResult;
import com.wjm.bootbook.entity.dto.JwtDTO;
import com.wjm.bootbook.entity.dto.review.SearchDTO;
import com.wjm.bootbook.entity.dto.review.TextReviewDTO;
import com.wjm.bootbook.entity.pojo.Article;
import com.wjm.bootbook.entity.vo.ArticleVO;
import com.wjm.bootbook.exception.CustomException;
import com.wjm.bootbook.service.ArticleService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.wjm.bootbook.entity.common.ElasticSearchConstant.*;
import static com.wjm.bootbook.entity.common.RabbitMqConstant.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author stephen wang
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    BaiduCheckService checkService;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/add")
    @RedisCache(key = "'article:'+#article.artId", read = 0)
    ResponseResult<ArticleVO> addArticle(@RequestBody Article article) {
        TextReviewDTO textReview = checkService.textReview(article.getArtContent(), article.getArtTitle());
        if (!BaiduCheckService.REVIEW_OK.equals(textReview.getConclusion())) {
            throw new CustomException(textReview.getReason());
        }

        boolean save = articleService.createNewArticle(article);
        if (save) {
            rabbitTemplate.convertAndSend(ES_EXCHANGE_NAME, KEYWORD_ADD, article);
            return ResponseResult.success(new ArticleVO(article.getArtId()));
        } else {
            throw new CustomException(ExceptionMessage.ADD_ARTICLE_ERROR.getMessage());
        }
    }

    @GetMapping("/{artId}")
    @RedisCache(key = "'article:'+#artId", read = 1)
    ResponseResult<Article> getArticleById(@PathVariable Long artId) {
        Article byId = articleService.getById(artId);
        if (byId == null) {
            throw new CustomException(ExceptionMessage.GET_ARTICLE_ERROR.getMessage());
        } else {
            return ResponseResult.success(byId);
        }
    }

    @DeleteMapping("/{artId}")
    @RedisCache(key = "'article:'+#artId", read = 0)
    ResponseResult<ArticleVO> deleteArticleById(@PathVariable Long artId) {
        boolean b = articleService.removeById(artId);
        if (b) {
            rabbitTemplate.convertAndSend(ES_EXCHANGE_NAME, KEYWORD_DELETE, artId);
            return ResponseResult.success(new ArticleVO(artId));
        } else {
            throw new CustomException(ExceptionMessage.DELETE_ARTICLE_ERROR.getMessage());
        }
    }

    @PutMapping("/update")
    @RedisCache(key = "'article:'+#artId", read = 0)
    ResponseResult<Article> updateArticleById(JwtDTO jwtDTO, @RequestBody Article article) {
        TextReviewDTO textReview = checkService.textReview(article.getArtContent(), article.getArtTitle());
        if (!BaiduCheckService.REVIEW_OK.equals(textReview.getConclusion())) {
            throw new CustomException(textReview.getReason());
        }

        Article article1 = articleService.userUpdateArticle(jwtDTO.getUserId(), article);
        if (article1 == null) {
            throw new CustomException(ExceptionMessage.UPDATE_ARTICLE_ERROR.getMessage());
        }
        rabbitTemplate.convertAndSend(ES_EXCHANGE_NAME, KEYWORD_UPDATE, article1);
        return ResponseResult.success(article1);
    }

    @GetMapping("/")
    ResponseResult<List<Article>> getAllArticles() {
        List<Article> list = articleService.list();
        return ResponseResult.success(list);
    }

    @GetMapping("/page")
    ResponseResult<Page<Article>> getPageArticle(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        articleService.page(page, null);

        return ResponseResult.success(page);
    }

    @PostMapping("/search")
    ResponseResult<List<Article>> searchKeywordArticle(@RequestBody SearchDTO searchDTO) {
        SearchRequest request = new SearchRequest(ES_ARTICLE_INDEX_NAME);
        request.source().query(QueryBuilders
                .matchQuery("all", searchDTO.getKeyword()));
        SearchResponse response;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new CustomException("查询 ES 出现错误");
        }
        SearchHit[] searchHits = response.getHits().getHits();
        ObjectMapper om = new ObjectMapper();
        List<Article> collect = Arrays.stream(searchHits).map(hit -> {
            try {
                return om.readValue(hit.getSourceAsString(), Article.class);
            } catch (JsonProcessingException e) {
                throw new CustomException(e.getMessage());
            }
        }).toList();
        return ResponseResult.success(collect);
    }
}
