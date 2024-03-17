package com.wjm.bootbook;

import com.wjm.bootbook.entity.pojo.Article;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class NoBootTest {
    @Test
    public void test01() {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("'article:'+#article.artId");
        EvaluationContext context = new StandardEvaluationContext();
        Article article = new Article();
        article.setArtId(10L);
        context.setVariable("article",article);
        System.out.println(expression.getValue(context).toString());
    }
}
