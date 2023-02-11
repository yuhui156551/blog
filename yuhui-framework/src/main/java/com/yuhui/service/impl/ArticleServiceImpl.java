package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.AddArticleDto;
import com.yuhui.domain.dto.ArticleDto;
import com.yuhui.domain.entity.Article;
import com.yuhui.domain.entity.ArticleTag;
import com.yuhui.domain.entity.Category;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.mapper.ArticleMapper;
import com.yuhui.service.ArticleService;
import com.yuhui.service.ArticleTagService;
import com.yuhui.service.CategoryService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.utils.RedisCache;
import com.yuhui.domain.vo.ArticleDetailVo;
import com.yuhui.domain.vo.ArticleListVo;
import com.yuhui.domain.vo.HotArticleVo;
import com.yuhui.domain.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yuhui.contants.SystemConstants.ARTICLE_STATUS_NORMAL;

/**
 * @author yuhui
 * @date 2022/12/31 19:43
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        // 构造条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 正式文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        // 浏览量进行降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        // 十条数据
        Page<Article> page = new Page(1, 10);
        page(page, queryWrapper);
        // 获取数据
        List<Article> articles = page.getRecords();
        // bean拷贝，封装成vo对象脱敏
//        ArrayList<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article, vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        // 返回
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 构造查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 若前端传了categoryId，则表示是分类下的查询，没传则是主页的查询
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0, Article::getCategoryId, categoryId);
        // 状态是正文
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        // 优先查询置顶文章，方案：根据isTop降序查询
        queryWrapper.orderByDesc(Article::getIsTop);
        // 分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // 分页查询结果
        List<Article> articles = page.getRecords();
        // 查询categoryName，如果看不懂，可以参考下方注释里面的增强for形式
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
        for (Article article : articles) {
            // 从redis获取viewCount
            Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT_KEY, article.getId().toString());
            article.setViewCount(viewCount.longValue());
        }
        // 封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        // 返回
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询文章
        Article article = getById(id);
        // 从redis获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT_KEY, id.toString());
        article.setViewCount(viewCount.longValue());
        // 封装成VO对象
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        // 根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }
        // 封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        // 更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT_KEY, id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional// 开启事务
    public ResponseResult add(AddArticleDto articleDto) {
        // 增加博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        // 填充sg_article_tag表
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        // 添加博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult pageArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 根据标题、摘要进行模糊查询
        queryWrapper.like(StringUtils.hasText(articleDto.getTitle()), Article::getTitle, articleDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleDto.getSummary()), Article::getSummary, articleDto.getSummary());
        // 分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // 封装pageVo返回
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        return ResponseResult.okResult(new PageVo(articleListVos, page.getTotal()));
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(AddArticleDto articleDto) {
        // 根据id修改博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getId, articleDto.getId());
        update(article, queryWrapper);
        // 标签不能为空
        if((articleDto.getTags() == null) || (articleDto.getTags().isEmpty())){
            throw new SystemException(AppHttpCodeEnum.TAG_NOT_NULL);
        }
        // 删除旧数据
//        articleTagService.removeById(article.getId());  没有主键，用不了
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagService.remove(lambdaQueryWrapper);
        // 填充sg_article_tag表
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());
        // 添加博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }
}
