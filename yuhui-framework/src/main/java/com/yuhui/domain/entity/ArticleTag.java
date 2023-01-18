package com.yuhui.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-01-10 10:30:07
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_article_tag")
@Accessors(chain = true)
public class ArticleTag implements Serializable{
   /* 序列化ID，相当于身份认证，主要用于程序的版本控制，保持不同版本的兼容性，在程序版本升级时避免程序报出版本不一致的错误
      如果没有定义一个名为serialVersionUID，类型为long的变量，Java序列化机制会根据编译的class自动生成一个serialVersionUID，即隐式声明。
      这种情况下，只有同一次编译生成的class才会生成相同的serialVersionUID 。
      此时如果对某个类进行修改的话，那么版本上面是不兼容的，就会出现反序列化报错的情况
    */
   private static final long serialVersionUID = 625337492348897098L;
    //文章id
    private Long articleId;
    //标签id
    private Long tagId;

}
