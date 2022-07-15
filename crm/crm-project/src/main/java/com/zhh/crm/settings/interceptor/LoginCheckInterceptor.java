package com.zhh.crm.settings.interceptor;

import com.zhh.crm.commons.Constants.Constant;
import com.zhh.crm.settings.bean.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否登录//没有登录重定向到登录页面
        HttpSession session = request.getSession();

        Object user = session.getAttribute(Constant.SESSION_USER);
        System.out.println(user);
        if(user==null){
            //没有登录
            response.sendRedirect(request.getContextPath()+"/");
            return false;
        }
        return true;
    }
}
