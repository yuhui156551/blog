package com.yuhui.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.TagDto;
import com.yuhui.domain.dto.TagListDto;
import com.yuhui.domain.entity.Tag;
import com.yuhui.domain.vo.PageVo;
import com.yuhui.domain.vo.TagVo;
import com.yuhui.service.TagService;
import com.yuhui.utils.BeanCopyUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        // 逻辑删除
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 数据回显
     */
    @GetMapping("/{id}")
    public ResponseResult selectTag(@PathVariable("id") Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(tag, TagDto.class));// 其实这里应该用vo返回
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody TagDto tagDto){
        return tagService.updateTag(tagDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<Tag> tags = tagService.list();
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(tags, TagVo.class));
    }
}