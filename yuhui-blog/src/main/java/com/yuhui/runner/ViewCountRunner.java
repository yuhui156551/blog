package com.yuhui.runner;

import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.entity.Article;
import com.yuhui.mapper.ArticleMapper;
import com.yuhui.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目启动时预处理
 * 项目启动时将数据库viewCount数据写入redis
 *
 * @author yuhui
 * @date 2023/1/7 19:02
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        // 查询博客信息
        List<Article> articles = articleMapper.selectList(null);
        // key: id  value: viewCount 存入map
        Map<String, Integer> viewCountMap = articles.stream().collect(Collectors.toMap(article -> article.getId().toString(),
                article -> article.getViewCount().intValue()));// long类型转换为int，否则例如1存入redis会变成1L，将无法进行递增操作
        // 存到redis中
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEW_COUNT_KEY, viewCountMap);
    }
}
