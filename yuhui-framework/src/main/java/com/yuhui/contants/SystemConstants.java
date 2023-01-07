package com.yuhui.contants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     *  分类是正常状态
     */
    public static final String CATEGORY_STATUS_NORMAL = "0";
    /**
     *  友链是正常状态
     */
    public static final String LINK_STATUS_NORMAL = "0";
    /**
     *  文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     *  友链评论
     */
    public static final String LINK_COMMENT = "1";
    /**
     *  文章围观量Redis键
     */
    public static final String ARTICLE_VIEW_COUNT_KEY = "article:viewCount:";
}