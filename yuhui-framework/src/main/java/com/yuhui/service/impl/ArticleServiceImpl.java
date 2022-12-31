package com.yuhui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.domain.entity.Article;
import com.yuhui.mapper.ArticleMapper;
import com.yuhui.service.ArticleService;
import org.springframework.stereotype.Service;

/**
 * @author yuhui
 * @date 2022/12/31 19:43
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}
