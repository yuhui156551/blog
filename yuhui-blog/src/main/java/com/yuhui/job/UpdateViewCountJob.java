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

    @Scheduled(cron = "0/5 * * * * ?")// * 0/10 * * * ?  0/5 * * * * ?
    public void updateViewCount() {
        // 定时任务：每隔10分钟把Redis中的浏览量更新到数据库中
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT_KEY);
        // 转化成单列集合，才可使用stream流
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        /**
         * 已解决空指针bug。
         * 原因：
         *  定时任务每次触发都会执行下面这行代码，会触发mp的更新字段时自动填充
         *  而自动填充的时候需要从SecurityHolder里面拿userId
         *  但是Holder里面还没有存储用户相关信息，所以报空指针错误
         * 解决方案：
         *  注释掉Article更新时自动填充注解
         */
        // 批量更新
        articleService.updateBatchById(articles);
        // 记录执行次数
//        System.out.println("定时任务执行了..." + count++);
    }
}
