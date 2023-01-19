package com.yuhui;

import com.yuhui.domain.entity.Article;
import com.yuhui.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuhui
 * @date 2023/1/19 19:09
 */
@SpringBootTest
public class DebugTest {
    @Resource
    private ArticleService articleService;

    @Test
    void updateTest(){
        ArrayList<Article> articles = new ArrayList<>();
        Article article1 = new Article(8L, 10);
        Article article2 = new Article(5L, 50);
        articles.add(article1);
        articles.add(article2);
        articleService.updateBatchById(articles);
    }
}
