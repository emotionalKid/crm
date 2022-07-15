package com.zhh.crm.workbench.controller;

import com.zhh.crm.commons.Constants.Constant;
import com.zhh.crm.commons.bean.ResponseMsg;
import com.zhh.crm.commons.util.CreateExcel;
import com.zhh.crm.commons.util.DateFormat;
import com.zhh.crm.commons.util.UUIDUtil;
import com.zhh.crm.settings.bean.User;
import com.zhh.crm.settings.service.UserService;
import com.zhh.crm.workbench.bean.Activity;
import com.zhh.crm.workbench.bean.ActivityRemark;
import com.zhh.crm.workbench.service.ActivityRemarkService;
import com.zhh.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    //跳转活动页面
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.selectAllUsers();
        request.setAttribute("userList" ,users);
        return "/workbench/activity/index";
    }
    //保存活动
    @ResponseBody
    @RequestMapping("/workbench/activity/saveActivity.do")
    public Object saveActivity(Activity activity, HttpSession session){
        User u = (User) session.getAttribute(Constant.SESSION_USER);
//        User u = new User()
        //封装
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateFormat.formatYMDHms(new Date()));
        activity.setCreateBy(u.getId());
        ResponseMsg responseMsg = new ResponseMsg();
        try {
            int i = activityService.saveActivity(activity);
            //大于0创建成功反之
            if(i>0){
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                responseMsg.setMessage("创建成功");
            }else {
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                responseMsg.setMessage("创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
            responseMsg.setMessage("创建失败");
        }
        return responseMsg;
    }

    //查询Activity和总条数
    @ResponseBody
    @RequestMapping("/workbench/activity/selectActivityByConditionForPage.do")
    public Object selectActivityByConditionForPage(String name,String owner,String startDate,String endDate, Integer pageNo, Integer pageSize){
        Map<String ,Object> responseMap;
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("name",name);
            map.put("owner",owner);
            map.put("startDate",startDate);
            map.put("endDate",endDate);
            map.put("beginNo",(pageNo-1)*pageSize);
            map.put("pageSize",pageSize);
            List<Activity> activityList = activityService.selectActivityByConditionForPage(map);
            int account= activityService.selectCount(map);
            responseMap = new HashMap<>();
            responseMap.put("activityList",activityList);
            responseMap.put("account",account);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseMap;
    }
        //删除
        @RequestMapping("/workbench/activity/deleteActivityById.do")
        @ResponseBody
        public Object deleteActivityById(String [] id){
            ResponseMsg responseMsg = new ResponseMsg();
            try {
                int i = activityService.deleteActivityById(id);
                if(i>0){
                    responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                }else {
                    responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                    responseMsg.setMessage("查询失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                responseMsg.setMessage("查询失败");
            }
            return responseMsg;
        }

        //根据id查询
        @RequestMapping("/workbench/activity/selectById.do")
        @ResponseBody
        public Object selectById(String id){
            Activity activity = activityService.selectById(id);
            return activity;
        }
        //修改activity
        @RequestMapping("/workbench/activity/updateActivityById.do")
        @ResponseBody
        public Object  updateActivityById(Activity activity,HttpSession session){
            User user = (User) session.getAttribute(Constant.SESSION_USER);
            activity.setEditBy(user.getId());
            activity.setEditTime(DateFormat.formatYMDHms(new Date()));
            ResponseMsg msg = new ResponseMsg();
            try {
                int i = activityService.updateActivityById(activity);
                if(i>0){
                    msg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                }else {
                    msg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                    msg.setMessage("修改失败");
                }

            } catch (Exception e) {
                e.printStackTrace();
                msg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                msg.setMessage("修改失败");
            }
            return msg;
        }
        @RequestMapping("/workbench/activity/selectAllActivity.do")
        public void selectAllActivity(HttpServletResponse response) throws IOException {
            List<Activity> activities = activityService.selectAllActivity();
            //根据查询到的activities创建excel
            HSSFWorkbook ws = new HSSFWorkbook();//创建工作普对象
            HSSFSheet sheet = ws.createSheet("activities");//创建工作表对象
            HSSFRow row = sheet.createRow(0);//创建行
            CreateExcel.insetFromSecond(row,sheet,activities);
//            row.createCell(0).setCellValue("ID");
//            row.createCell(1).setCellValue("所有者");
//            row.createCell(2).setCellValue("名称");
//            row.createCell(3).setCellValue("开始日期");
//            row.createCell(4).setCellValue("结束日期");
//            row.createCell(5).setCellValue("成本");
//            row.createCell(6).setCellValue("描述");
//            row.createCell(7).setCellValue("创建时间");
//            row.createCell(8).setCellValue("创建者");
//            row.createCell(9).setCellValue("修改时间");
//            row.createCell(10).setCellValue("修改者");
//
//            if(activities!=null&&activities.size()>0){
//                Activity activity=null;
//                for(int i=0;i<activities.size();i++){
//                    activity=activities.get(i);
//                    row = sheet.createRow(i+1);//创建行
//                    row.createCell(0).setCellValue(activity.getId());
//                    row.createCell(1).setCellValue(activity.getOwner());
//                    row.createCell(2).setCellValue(activity.getName());
//                    row.createCell(3).setCellValue(activity.getStartDate());
//                    row.createCell(4).setCellValue(activity.getEndDate());
//                    row.createCell(5).setCellValue(activity.getCost());
//                    row.createCell(6).setCellValue(activity.getDescription());
//                    row.createCell(7).setCellValue(activity.getCreateTime());
//                    row.createCell(8).setCellValue(activity.getCreateBy());
//                    row.createCell(9).setCellValue(activity.getEditTime());
//                    row.createCell(10).setCellValue(activity.getEditBy());
//                }
//            }
            //通过流创建excel文件
//            FileOutputStream os = new FileOutputStream("C:\\q\\activities.xlsx");
//            ws.write(os);
//            os.close();

            //返回到页面一个下载excel
            response.setContentType("application/octet-stream;charset=UTF-8");
                response.addHeader("Content-Disposition","attachment;filename=activity.xls");
            OutputStream out =response.getOutputStream();

//            //读取创建的excel
//            FileInputStream is = new FileInputStream("C:\\q\\activities.xlsx");
//            byte[] bytes = new byte[1024];
//            int len=0;
//            if((len=is.read(bytes))!=-1){
//                out.write(bytes,0, len);
//            }
            ws.write(out);
            //关闭资源
            ws.close();
            out.flush();
        }
//        @ResponseBody
        @RequestMapping("/workbench/activity/selectByIds.do")
        public void selectByIds(String [] id,HttpServletResponse response) throws IOException{
            List<Activity> activities = activityService.selectByIds(id);
            //根据查询到的activities创建excel
            HSSFWorkbook ws = new HSSFWorkbook();//创建工作普对象
            HSSFSheet sheet = ws.createSheet("activities");//创建工作表对象
            HSSFRow row = sheet.createRow(0);//创建行
            CreateExcel.insetFromSecond(row,sheet,activities);
            //返回到页面一个下载excel
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.addHeader("Content-Disposition","attachment;filename=activity.xls");
            OutputStream out =response.getOutputStream();
            ws.write(out);
            //关闭资源
            ws.close();
            out.flush();
//            return "";
        }
        @RequestMapping("/workbench/activity/inputActivityBatch.do")
        @ResponseBody
        public Object inputActivityBatch(MultipartFile activityFile,HttpSession session)throws IOException{
            ResponseMsg responseMsg = new ResponseMsg();
            try {
//                //获取文件，写入磁盘
//                String originalFilename = activityFile.getOriginalFilename();
//                File file = new File("C:\\q\\b\\"+originalFilename);
//                activityFile.transferTo(file);
//                //拿到磁盘文件，解析文件
//                FileInputStream fis = new FileInputStream("C:\\q\\b\\" + originalFilename);
                InputStream is = activityFile.getInputStream();
                HSSFWorkbook hw = new HSSFWorkbook(is);//所有fis文件信息
                //获取页
                HSSFSheet sheet = hw.getSheetAt(0);//获取第一页所有信息
                HSSFRow row=sheet.createRow(0);
//                HSSFRow row=null;
//                HSSFCell cell=null;
//                List<Activity> activities=new ArrayList<>();
//                Activity activity=null;
//                User user = (User) session.getAttribute(Constant.SESSION_USER);
//                responseMsg = new ResponseMsg();
//                //获取行.一行封装到一个activity对象里
//                for (int i=1;i<=sheet.getLastRowNum();i++){
//                    row = sheet.getRow(i);//里面有i行的信息
//                     activity = new Activity();
//                     //id自动生成
//                    //所有者和创建者id从session中User里拿
//                    //创建时间现在
//                     activity.setId(UUIDUtil.getUUID());
//                     activity.setOwner(user.getId());
//                     activity.setCreateTime(DateFormat.formatYMDHms(new Date()));
//                     activity.setCreateBy(user.getId());
//                    for (int j=0;j<row.getLastCellNum();j++){
//                        cell = row.getCell(j);//j单元格信息
//                        String cellValue="";//单元格值
//                        if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
//                            cellValue=cell.getStringCellValue();
//                        }else if(cell.getCellType()== HSSFCell.CELL_TYPE_BOOLEAN){
//                            cellValue=cell.getBooleanCellValue()+"";
//                        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
//                            cellValue=cell.getNumericCellValue()+"";
//                        }else if(cell.getCellType()== HSSFCell.CELL_TYPE_FORMULA){
//                            cellValue=cell.getCellFormula()+"";
//                        }else {
//                            cellValue="";
//                        }
//                        if(j==0){
//                            activity.setName(cellValue);
//                        }else if(j==1){
//                            activity.setStartDate(cellValue);
//                        }else if(j==2){
//                            activity.setEndDate(cellValue);
//                        }else if(j==3){
//                            activity.setCost(cellValue);
//                        }else if(j==4){
//                            activity.setDescription(cellValue);
//                        }
//                    }
//                    activities.add(activity);
//                }
                List<Activity> activities = CreateExcel.parseExcel(row, sheet, session);
                //插入activity到数据库
                int ret = activityService.insertActivityBatch(activities);
                if(ret>0){
                    responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
                    responseMsg.setOtherMsg(ret);
                }else {
                    responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                    responseMsg.setMessage("导入失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseMsg.setStateCode(Constant.RETURN_OBJECT_CODE_FAIL);
                responseMsg.setMessage("导入失败");
            }
            return responseMsg;
        }

    /**
     * 查询市场活动详情页面需要的数据
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/selectDetailData.do")
        public String selectDetailData(String id,HttpServletRequest request){
        //查询数据
        Activity activity = activityService.selectActivityById(id);
        System.out.println(activity);
        List<ActivityRemark> activityRemarks = activityRemarkService.selectRemarkById(id);
        //放入请求域中
        request.setAttribute("activity",activity);
        request.setAttribute("activityRemarks",activityRemarks);
        return "/workbench/activity/detail";
    }
}
