package com.yuhui.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.TagListDto;
import com.yuhui.domain.entity.Tag;
import com.yuhui.domain.vo.PageVo;
import com.yuhui.domain.vo.TagVo;
import com.yuhui.service.TagService;
import com.yuhui.utils.BeanCopyUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        // 获取tag
        Tag tag = tagService.getById(id);
        // 转成vo并返回
        return ResponseResult.okResult(BeanCopyUtils.copyBean(tag, TagVo.class));
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody TagVo tagVo){// 用vo不太好，但是恰好vo这三个字段符合前端传递的三个字段，我偷个小懒
        return tagService.updateTag(tagVo);
    }
}