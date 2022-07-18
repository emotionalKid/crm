package com.zhh.crm.workbench.activity.controller;

import com.zhh.crm.commons.Constants.Constant;
import com.zhh.crm.commons.bean.ResponseMsg;
import com.zhh.crm.commons.util.DateFormat;
import com.zhh.crm.commons.util.UUIDUtil;
import com.zhh.crm.settings.bean.User;
import com.zhh.crm.workbench.activity.bean.ActivityRemark;
import com.zhh.crm.workbench.activity.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/addRemark.do")
    @ResponseBody
    public Object addRemark(ActivityRemark remark, HttpSession session){
        //封装数据
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateFormat.formatYMDHms(new Date()));
        remark.setEditFlag(Constant.REMARK_EDIT_FLAG_NO_EDITED);
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            //插入
            int i = activityRemarkService.insertActivityRemark(remark);
            if(i>0){
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                responseMsg.setOtherMsg(remark);
            }else {
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                responseMsg.setMessage("插入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
            responseMsg.setMessage("插入失败");
        }
        return responseMsg;
    }

    /**
     * 删除市场活动备注/通过id
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/workbench/activity/deleteRemark.do")
    public Object deleteRemark(String id){
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            int i = activityRemarkService.deleteActivityRemark(id);
            if(i>0){
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                responseMsg.setMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
            responseMsg.setMessage("删除失败");
        }
        return responseMsg;
    }

    /**
     * 修改remark
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/updateRemark.do")
    @ResponseBody
    public Object updateRemark(ActivityRemark remark,HttpSession session){
        ResponseMsg responseMsg = new ResponseMsg();
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        remark.setEditFlag(Constant.REMARK_EDIT_FLAG_YES_EDITED);
        remark.setEditBy(user.getId());
        remark.setEditTime(DateFormat.formatYMDHms(new Date()));
        try {
            int i = activityRemarkService.updateActivityRemark(remark);
            if(i>0){
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                responseMsg.setOtherMsg(remark);
            }else {
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                responseMsg.setMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
            responseMsg.setMessage("删除失败");
        }
        return responseMsg;
    }
}
