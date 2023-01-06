package com.yuhui.service;

import com.yuhui.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuhui
 * @date 2023/1/6 12:25
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
