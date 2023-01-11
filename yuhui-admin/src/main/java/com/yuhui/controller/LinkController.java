package com.yuhui.controller;

import com.yuhui.annotation.SystemLog;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.LinkDto;
import com.yuhui.domain.dto.LinkListDto;
import com.yuhui.domain.dto.LinkStatusDto;
import com.yuhui.domain.entity.Link;
import com.yuhui.domain.vo.LinkVo;
import com.yuhui.service.LinkService;
import com.yuhui.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author yuhui
 * @date 2023/1/11 16:17
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Resource
    private LinkService linkService;

    @GetMapping("/list")
    @SystemLog(businessName = "分页查询友链列表")
    public ResponseResult list(Integer pageNum, Integer pageSize, LinkListDto linkListDto){
        return linkService.pageLinkList(pageNum, pageSize, linkListDto);
    }

    @GetMapping("{id}")
    @SystemLog(businessName = "回显友链数据")
    public ResponseResult selectLink(@PathVariable("id") Long id){
        return ResponseResult.okResult(BeanCopyUtils.copyBean(linkService.getById(id), LinkVo.class));
    }

    @PutMapping
    @SystemLog(businessName = "修改友链数据")
    public ResponseResult updateLink(@RequestBody LinkDto linkDto){
        return linkService.updateLink(linkDto);
    }

    @PostMapping
    @SystemLog(businessName = "新增友链")
    public ResponseResult addLink(@RequestBody LinkDto linkDto){
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @SystemLog(businessName = "删除友链")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        return ResponseResult.okResult(linkService.removeById(id));
    }

    @PutMapping("/changeLinkStatus")
    @SystemLog(businessName = "修改友链状态")
    public ResponseResult changeLinkStatus(@RequestBody LinkStatusDto linkStatusDto){
        return linkService.changeLinkStatus(linkStatusDto);
    }
}
