package com.yuhui.filter;

import com.alibaba.fastjson.JSON;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.LoginUser;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.utils.JwtUtil;
import com.yuhui.utils.RedisCache;
import com.yuhui.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author yuhui
 * @date 2023/1/4 13:15
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)) {
            // token不存在，说明是第一次登录，直接放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析获取userId
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // token超时 or token非法
            // 响应告诉前端需要重新登录
            // 不能直接抛异常，因为统一异常处理在controller层，这里的异常不会被捕获
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            // 这里是filter，并非像MVC那般会处理返回信息转成JSON，所以需要我们手动处理
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();
        // 从redis获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("blog:login:" + userId);
        if(Objects.isNull(loginUser)){
            // 如果获取失败，说明登录过期
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        // 存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
