package com.wjm.bootbook.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wjm.bootbook.entity.pojo.Article;

/**
 * @author stephen wang
 */
public interface ArticleService extends IService<Article> {
    Article userUpdateArticle(Long userId, Article article);

    boolean createNewArticle(Article article);
}
