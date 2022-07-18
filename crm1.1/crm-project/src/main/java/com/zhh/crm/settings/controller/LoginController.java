package com.zhh.crm.settings.controller;

import com.zhh.crm.commons.Constants.Constant;
import com.zhh.crm.commons.bean.ResponseMsg;
import com.zhh.crm.commons.util.DateFormat;
import com.zhh.crm.settings.bean.User;
import com.zhh.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//登入页面controller
@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    /**
     * 跳转登录页面
     * @return
     */
    @RequestMapping("/settings/qx/user/loginPage.do")
    public String loginPage(){
        return "settings/qx/user/login";
    }

    /**
     * 用户登录功能
     * 如果账号密码正确/且状态正常/没有超时/ip没有受限的情况下登录成功
     * 是否记住密码，记住密码10天内免登录
     * @return
     */
    @ResponseBody
    @RequestMapping("/settings/qx/user/login.do")
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpSession session, HttpServletResponse response){
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        map.put("idRemPwd",isRemPwd);
        User user = userService.selectByActPwd(map);
        ResponseMsg msg = new ResponseMsg();
        if (user!=null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = DateFormat.formatYMDHms(new Date());
            if (now.compareTo(user.getExpireTime()) > 0) {
                //账号以过期，登入失败
                msg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                msg.setMessage("账号已过期，登入失败");
            } else if ("0".equals(user.getLockState())) {
                //状态被锁定,登入失败
                msg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                msg.setMessage("状态被锁定,登入失败");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                //ip受限，登录失败
                msg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                msg.setMessage("ip受限，登录失败");
            } else {
                //登入成功
                msg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                msg.setMessage("登入成功");
                //把user放入session完成每个功能页面显示用户名
                session.setAttribute(Constant.SESSION_USER,user);
                //是否记住密码
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                    c1.setMaxAge(Constant.COOLIE_MAX_AGE_TEN);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                    c2.setMaxAge(Constant.COOLIE_MAX_AGE_TEN);
                    response.addCookie(c2);
                }else {
                    Cookie c1 = new Cookie("loginAct", "1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }else {
            //账号密码错误
            msg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
            msg.setMessage("账号/密码错误，登录失败");
        }
        return msg;
    }
    /**
     * 安全退出功能
     * 用户点击安全退出，且确定，则销毁cookie/session，跳转首页/登陆页面
     *
     */
    @RequestMapping("/settings/qx/user/logOut.do")
    public String logOut(HttpServletResponse response,HttpSession session){
        Cookie c1 = new Cookie("loginAct", "1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        session.invalidate();
        return "redirect:/";
    }
}
