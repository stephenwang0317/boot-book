package com.wjm.bootbook.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wjm.bootbook.entity.common.ExceptionMessage;
import com.wjm.bootbook.entity.common.ResponseResult;
import com.wjm.bootbook.entity.dto.JwtDTO;
import com.wjm.bootbook.entity.pojo.Article;
import com.wjm.bootbook.entity.vo.ArticleVO;
import com.wjm.bootbook.exception.CustomException;
import com.wjm.bootbook.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    ResponseResult<ArticleVO> addArticle(@RequestBody Article article) {
        boolean save = articleService.createNewArticle(article);
        if (save) {
            return ResponseResult.success(new ArticleVO(article.getArtId()));
        } else {
            throw new CustomException(ExceptionMessage.ADD_ARTICLE_ERROR.getMessage());
        }
    }

    @GetMapping("/{artId}")
    ResponseResult<Article> getArticleById(@PathVariable Long artId) {
        Article byId = articleService.getById(artId);
        if (byId == null) {
            throw new CustomException(ExceptionMessage.GET_ARTICLE_ERROR.getMessage());
        } else {
            return ResponseResult.success(byId);
        }
    }

    @DeleteMapping("/{artId}")
    ResponseResult<ArticleVO> deleteArticleById(@PathVariable Long artId) {
        boolean b = articleService.removeById(artId);
        if (b) {
            return ResponseResult.success(new ArticleVO(artId));
        } else {
            throw new CustomException(ExceptionMessage.DELETE_ARTICLE_ERROR.getMessage());
        }
    }

    @PutMapping("/update")
    ResponseResult<Article> updateArticleById(JwtDTO jwtDTO, @RequestBody Article article) {
        Article article1 = articleService.userUpdateArticle(jwtDTO.getUserId(), article);
        if (article1 == null) {
            throw new CustomException(ExceptionMessage.UPDATE_ARTICLE_ERROR.getMessage());
        }
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
}
