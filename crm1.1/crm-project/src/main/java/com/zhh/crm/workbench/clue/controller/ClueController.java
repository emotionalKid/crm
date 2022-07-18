package com.zhh.crm.workbench.clue.controller;

import com.zhh.crm.commons.Constants.Constant;
import com.zhh.crm.commons.bean.ResponseMsg;
import com.zhh.crm.commons.util.DateFormat;
import com.zhh.crm.commons.util.UUIDUtil;
import com.zhh.crm.settings.bean.DicValue;
import com.zhh.crm.settings.bean.User;
import com.zhh.crm.settings.service.DicValueService;
import com.zhh.crm.settings.service.UserService;
import com.zhh.crm.workbench.clue.bean.Clue;
import com.zhh.crm.workbench.clue.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    /**
     * 查询clue页面所需要的所有字典数据
     * @return
     */
    @RequestMapping("/workbench/clue/queryClueByData.do")
    public String queryClueByData(HttpServletRequest request){
        //查询所需要的数据
        List<User> users = userService.selectAllUsers();
        List<DicValue> appellation = dicValueService.selectDicValueByTypeCode("appellation");
        List<DicValue> source = dicValueService.selectDicValueByTypeCode("source");
        List<DicValue> clueState = dicValueService.selectDicValueByTypeCode("clueState");
        //放入请求域
        request.setAttribute("users",users);
        request.setAttribute("appellation",appellation);
        request.setAttribute("source",source);
        request.setAttribute("clueState",clueState);
        return "/workbench/clue/index";
    }
    @RequestMapping("/workbench/clue/saveClue.do")
    @ResponseBody
    public Object saveClue(Clue clue, HttpSession session){
        //封装数据
        User user = (User) session.getAttribute(Constant.SESSION_USER);
            clue.setId(UUIDUtil.getUUID());
            clue.setCreateBy(user.getId());
            clue.setCreateTime(DateFormat.formatYMDHms(new Date()));
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            //插入
            int i = clueService.saveClue(clue);
            if(i>0){
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                responseMsg.setMessage("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
            responseMsg.setMessage("保存失败");
        }
        return responseMsg;
    }
    /**
     * 根据条件分页查询
     * @return
     */
    @ResponseBody
    @RequestMapping("/workbench/clue/selectClueByConditionAndAccount.do")
    public Object selectClueByConditionAndAccount(String fullname,String company,String phone,String source,
                                                  String owner,String mphone,String state,Integer pageNo,Integer pageSize){
        Map<String,Object> retMap=new HashMap<>();
        try {
            Map<String ,Object> map=new HashMap<>();
            //封装数据
            map.put("fullname",fullname);
            map.put("company",company);
            map.put("phone",phone);
            map.put("source",source);
            map.put("owner",owner);
            map.put("mphone",mphone);
            map.put("state",state);
//            map.put("PageNo",(pageNo-1)*pageSize);
            map.put("beginNo",(pageNo-1)*pageSize);
            map.put("pageSize",pageSize);
            List<Clue> clues = clueService.selectClueByConditionForPage(map);
            int account = clueService.selectClueAccount(map);
            retMap.put("clues",clues);
            retMap.put("account",account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMap;
    }
    @RequestMapping("/workbench/clue/selectDetailPageData.do")
    public String selectDetailPageData(String id,HttpServletRequest request){
        //获取数据
        Clue clue = clueService.selectClueById(id);
        List<ClueRemark> clueRemarks = clueRemarkService.selectClueRemarkById(id);
        List<Activity> activities = activityService.selectActivityContactClueID(id);
        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarks",clueRemarks);
        request.setAttribute("activities",activities);
        return "/workbench/clue/detail";
    }
}
