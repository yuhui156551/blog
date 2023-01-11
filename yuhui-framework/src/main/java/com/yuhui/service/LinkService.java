package com.yuhui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.LinkDto;
import com.yuhui.domain.dto.LinkListDto;
import com.yuhui.domain.dto.LinkStatusDto;
import com.yuhui.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-01-02 20:04:43
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto);

    ResponseResult updateLink(LinkDto linkDto);

    ResponseResult changeLinkStatus(LinkStatusDto linkStatusDto);
}
