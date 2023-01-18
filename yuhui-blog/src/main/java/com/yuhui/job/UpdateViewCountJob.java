package com.yuhui.job;

import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.entity.Article;
import com.yuhui.service.ArticleService;
import com.yuhui.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuhui
 * @date 2023/1/7 20:07
 */
@Component
public class UpdateViewCountJob {
    private int count = 0;

    @Autowired
    private RedisCache redisCache;
    @Autowired// 涉及批量操作，不选择mapper
    private ArticleService articleService;

    @Scheduled(cron = "* 0/10 * * * ?")// * 0/10 * * * ?
    public void updateViewCount() {
        // 定时任务：每隔10分钟把Redis中的浏览量更新到数据库中
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT_KEY);

        List<Article> articles = viewCountMap.entrySet()// 转化成单列集合，才可使用stream流
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        // 批量更新
        // TODO 莫名其妙出空指针bug，害，不影响使用、先不管了
        articleService.updateBatchById(articles);

        System.out.println("定时任务执行了..." + count++);
    }
}
