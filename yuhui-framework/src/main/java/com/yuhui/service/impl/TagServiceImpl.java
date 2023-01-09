package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.TagListDto;
import com.yuhui.domain.entity.Tag;
import com.yuhui.domain.vo.PageVo;
import com.yuhui.domain.vo.TagVo;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.mapper.TagMapper;
import com.yuhui.service.TagService;
import com.yuhui.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-01-08 13:39:11
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        // 搜索框里面填了什么，就根据name去查对应的tag，若什么都没填，直接返回所有标签
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        // 前端暂未实现根据备注名查询
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());
        // 分页查询
        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // 返回
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagListDto tagListDto) {
        Tag tag = new Tag();
        // 内容不能为空
        if(!StringUtils.hasText(tagListDto.getName()) || !StringUtils.hasText(tagListDto.getRemark())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // 不能重复（可以不写，一般后台自己管理，不会添加已有的标签其实）
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        if(count(queryWrapper.eq(Tag::getName, tagListDto.getName())) > 0){
            throw new SystemException(AppHttpCodeEnum.NAME_EXIST);
        }
        // 注意此处需要new一个新的wrapper
        LambdaQueryWrapper<Tag> queryWrapper2 = new LambdaQueryWrapper<>();
        if(count(queryWrapper2.eq(Tag::getRemark, tagListDto.getRemark())) > 0){
            throw new SystemException(AppHttpCodeEnum.REMARK_EXIST);
        }
        // 设置值
        tag.setName(tagListDto.getName());
        tag.setRemark(tagListDto.getRemark());
        // 保存并返回
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateTag(TagVo tagVo) {
        // 内容不能为空
        if(!StringUtils.hasText(tagVo.getName()) || !StringUtils.hasText(tagVo.getRemark())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // TODO 现在想实现：修改标签，修改之后的标签和备注不能和数据库里面的重复
        //  但是现在遇到的问题是：如果和自己本身旧数据一样的话，也会抛重复异常，导致判断失误
        //  但是不巧的是：爷实现了，代码如下
        // 根据id获取数据库tag
        Tag oldTag = getById(tagVo.getId());
        // 如果传过来的name和oldTag里的name不一样，说明进行了修改
        if(!oldTag.getName().equals(tagVo.getName())){
            // 判断name是否重复
            LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
            if(count(queryWrapper.eq(Tag::getName, tagVo.getName())) > 0){
                throw new SystemException(AppHttpCodeEnum.NAME_EXIST);
            }
        }
        if(!oldTag.getRemark().equals(tagVo.getRemark())){
            // 判断remark是否重复
            LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
            if(count(queryWrapper.eq(Tag::getRemark, tagVo.getRemark())) > 0){
                throw new SystemException(AppHttpCodeEnum.REMARK_EXIST);
            }
        }
        // 设置值
        Tag tag = BeanCopyUtils.copyBean(tagVo, Tag.class);
        // 根据id更新tag
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Tag::getId, tagVo.getId());
        update(tag, lambdaQueryWrapper);
        // 返回
        return ResponseResult.okResult();
    }
}
