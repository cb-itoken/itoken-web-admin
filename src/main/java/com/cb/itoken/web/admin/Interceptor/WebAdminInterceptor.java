package com.cb.itoken.web.admin.Interceptor;

import com.cb.itoken.common.domain.TbSysUser;
import com.cb.itoken.common.utils.CookieUtils;
import com.cb.itoken.common.utils.MapperUtils;
import com.cb.itoken.web.admin.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class WebAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = CookieUtils.getCookieValue(request, "token");

        // token 为空表示一定没有登录
        if(StringUtils.isBlank(token)){
            try {
                response.sendRedirect("http://localhost:8503/login?url=http://localhost:8601");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        HttpSession session = request.getSession();
        TbSysUser tbSysUser = (TbSysUser) session.getAttribute("tbSysUser");

        // 已登录状态
        if(tbSysUser != null){
            if(modelAndView != null){
                modelAndView.addObject("tbSysUser", tbSysUser);
            }
        }

        // 未登录状态
        else {
            String token = CookieUtils.getCookieValue(request, "token");
            if(StringUtils.isNotBlank(token)){
                String loginCode = redisService.get(token);
                if(StringUtils.isNotBlank(loginCode)){
                    String json = redisService.get(loginCode);
                    if(StringUtils.isNotBlank(json)){
                        try {
                            // 已登录状态, 创建局部会话
                            tbSysUser = MapperUtils.json2pojo(json, TbSysUser.class);
                            if(modelAndView != null){
                                modelAndView.addObject("tbSysUser", tbSysUser);
                            }
                            request.getSession().setAttribute("tbSysUser", tbSysUser);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

        // 二次确认是否有用户信息
        if(tbSysUser == null){
            try {
                response.sendRedirect("http://localhost:8503/login?url=http://localhost:8601");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}