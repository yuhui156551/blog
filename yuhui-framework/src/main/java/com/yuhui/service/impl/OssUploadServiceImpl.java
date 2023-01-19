package com.yuhui.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.LoginUser;
import com.yuhui.domain.entity.User;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.service.UploadService;
import com.yuhui.service.UserService;
import com.yuhui.utils.PathUtils;
import com.yuhui.utils.SecurityUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author yuhui
 * @date 2023/1/6 12:25
 */
@Service
@Component
@ConfigurationProperties(prefix = "oss")
public class OssUploadServiceImpl implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        // 获取原始文件名并对后缀进行判断
        String originalFilename = img.getOriginalFilename();
        if(!(originalFilename.endsWith(".jpg") || originalFilename.endsWith(".png"))){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        // TODO 自己思考的，外加自己实现：上传之前，删除原来的头像文件，防止堆压空间
        //  但是TMD实现失败了...具体看方法注释
//        deleteOss();
        // 获取文件路径
        String filePath = PathUtils.generateFilePath(originalFilename);
        // 上传文件到OSS
        String url = uploadOss(img, filePath);// 期望格式：2023/1/6/xxx.jpg
        return ResponseResult.okResult(url);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     *  删除之前的头像文件
     *  TODO 不知道为什么拿不到loginUser，其他地方都正常拿，真是奇了怪了
     */
    private void deleteOss(){
        Configuration cfg = new Configuration(Region.region0());
        // 获取用户数据库里面的头像文件外链
        String avatar = null;
        try {
            // 此行报错
            avatar = SecurityUtils.getLoginUser().getUser().getAvatar();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        String key = avatar;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
            // 后台记录日志
            System.out.println("删除成功！");
        } catch (QiniuException ex) {
            // 如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 上传头像文件
     * @param imgFile 头像文件
     * @param filePath 文件路径
     * @return
     */
    private String uploadOss(MultipartFile imgFile, String filePath) {
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        UploadManager uploadManager = new UploadManager(cfg);

        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {

            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                // 返回外链
                // TODO 测试域名创建时间：2023-01-06 10:27:35，30天之后自动回收，到时候考虑买个域名
                return "http://ro1llou5e.hn-bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    // ignore
                }
            }
        } catch (Exception ex) {
            // ignore
        }
        return "www";
    }
}
