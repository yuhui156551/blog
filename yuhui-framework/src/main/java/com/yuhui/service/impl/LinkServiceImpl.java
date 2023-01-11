package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.LinkDto;
import com.yuhui.domain.dto.LinkListDto;
import com.yuhui.domain.dto.LinkStatusDto;
import com.yuhui.domain.entity.Link;
import com.yuhui.domain.vo.PageVo;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.mapper.LinkMapper;
import com.yuhui.service.LinkService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.domain.vo.LinkVo;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-01-02 20:04:43
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        // 查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList = list(queryWrapper);
        // 封装成VO返回
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto) {
        // 根据名称模糊查询
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(linkListDto.getName()), Link::getName, linkListDto.getName());
        // 根据状态查询
        queryWrapper.eq(StringUtils.hasText(linkListDto.getStatus()), Link::getStatus, linkListDto.getStatus());
        // 封装成pageVo返回
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult updateLink(LinkDto linkDto) {
        // 名字、描述不能为空
        if(!StringUtils.hasText(linkDto.getName()) || !StringUtils.hasText(linkDto.getDescription())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // 根据id修改
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getId, linkDto.getId());
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        update(link, queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(LinkStatusDto linkStatusDto) {
        // 根据id修改状态
        Link link = getById(linkStatusDto.getId());
        link.setStatus(linkStatusDto.getStatus());
        // 单单设置值可不够，还要操作数据库
        updateById(link);
        // 返回状态信息
        return ResponseResult.okResult();
    }
}
