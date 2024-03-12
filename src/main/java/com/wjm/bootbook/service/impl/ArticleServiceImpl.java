package com.wjm.bootbook.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjm.bootbook.entity.common.ExceptionMessage;
import com.wjm.bootbook.entity.common.ResponseResult;
import com.wjm.bootbook.entity.pojo.Article;
import com.wjm.bootbook.exception.CustomException;
import com.wjm.bootbook.mapper.ArticleMapper;
import com.wjm.bootbook.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author stephen wang
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public Article userUpdateArticle(Long userId, Article article) {
        Article queriedArt = articleMapper.selectById(article.getArtId());
        if (queriedArt == null) {
            throw new CustomException(ExceptionMessage.NO_SUCH_ARTICLE.getMessage());
        }
        if (!userId.equals(article.getArtAuthor()) ||
            !queriedArt.getArtAuthor().equals(article.getArtAuthor())) {
            throw new CustomException(ExceptionMessage.CANT_UPDATE_ARTICLE.getMessage());
        }

        queriedArt.setArtTitle(article.artTitle);
        queriedArt.setArtContent(article.artContent);
        queriedArt.setArtModified(System.currentTimeMillis());
        boolean b = updateById(queriedArt);
        return b ? queriedArt : null;
    }

    @Override
    public boolean createNewArticle(Article article) {
        article.setArtCreated(System.currentTimeMillis());
        article.setArtModified(article.artCreated);
        article.setArtLike(0);
        return this.save(article);
    }
}
