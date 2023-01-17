package com.yuhui.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuhui.annotation.SystemLog;
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
    @SystemLog(businessName = "分页查询标签列表")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    @SystemLog(businessName = "添加标签")
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @DeleteMapping("/{ids}")
    @SystemLog(businessName = "（批量）删除标签")
    public ResponseResult deleteTag(@PathVariable("ids") List<Long> ids){
        tagService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "数据回显")
    public ResponseResult selectTag(@PathVariable("id") Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(tag, TagDto.class));// 其实这里应该用vo返回
    }

    @PutMapping
    @SystemLog(businessName = "更新标签")
    public ResponseResult updateTag(@RequestBody TagDto tagDto){
        return tagService.updateTag(tagDto);
    }

    @GetMapping("/listAllTag")
    @SystemLog(businessName = "获取所有标签")
    public ResponseResult listAllTag(){
        List<Tag> tags = tagService.list();
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(tags, TagVo.class));
    }
}