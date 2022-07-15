package com.zhh.crm.commons.util;

import com.zhh.crm.commons.Constants.Constant;
import com.zhh.crm.commons.bean.ResponseMsg;
import com.zhh.crm.settings.bean.User;
import com.zhh.crm.workbench.bean.Activity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateExcel {
    //创建excel文件
    public static void insetFromSecond(HSSFRow row, HSSFSheet sheet, List<Activity> activities){
        if(activities!=null&&activities.size()>0) {
            Activity activity = null;
            for (int i = 0; i < activities.size(); i++) {
                activity = activities.get(i);
                if (i == 0) {
                    row = sheet.createRow(i);//创建行
                    row.createCell(0).setCellValue("ID");
                    row.createCell(1).setCellValue("所有者");
                    row.createCell(2).setCellValue("名称");
                    row.createCell(3).setCellValue("开始日期");
                    row.createCell(4).setCellValue("结束日期");
                    row.createCell(5).setCellValue("成本");
                    row.createCell(6).setCellValue("描述");
                    row.createCell(7).setCellValue("创建时间");
                    row.createCell(8).setCellValue("创建者");
                    row.createCell(9).setCellValue("修改时间");
                    row.createCell(10).setCellValue("修改者");
                    continue;
                }else {
                row = sheet.createRow(i);//创建行
                row.createCell(0).setCellValue(activity.getId());
                row.createCell(1).setCellValue(activity.getOwner());
                row.createCell(2).setCellValue(activity.getName());
                row.createCell(3).setCellValue(activity.getStartDate());
                row.createCell(4).setCellValue(activity.getEndDate());
                row.createCell(5).setCellValue(activity.getCost());
                row.createCell(6).setCellValue(activity.getDescription());
                row.createCell(7).setCellValue(activity.getCreateTime());
                row.createCell(8).setCellValue(activity.getCreateBy());
                row.createCell(9).setCellValue(activity.getEditTime());
                row.createCell(10).setCellValue(activity.getEditBy());
                }
            }
        }
    }


    public static List<Activity> parseExcel(HSSFRow row, HSSFSheet sheet, HttpSession session){
//        String originalFilename = activityFile.getOriginalFilename();
//        File file = new File("C:\\q\\b\\"+originalFilename);
//        activityFile.transferTo(file);
        //拿到磁盘文件，解析文件
//        FileInputStream fis = new FileInputStream("C:\\q\\b\\" + originalFilename);
//        HSSFWorkbook hw = new HSSFWorkbook(fis);//所有fis文件信息
        //获取页
//        sheet = hw.getSheetAt(0);//获取第一页所有信息
        HSSFCell cell=null;
        List<Activity> activities=new ArrayList<>();
        Activity activity=null;
        User user = (User) session.getAttribute(Constant.SESSION_USER);
//        responseMsg = new ResponseMsg();
        //获取行.一行封装到一个activity对象里
        for (int i=1;i<=sheet.getLastRowNum();i++){
            row = sheet.getRow(i);//里面有i行的信息
            activity = new Activity();
            //id自动生成
            //所有者和创建者id从session中User里拿
            //创建时间现在
            activity.setId(UUIDUtil.getUUID());
            activity.setOwner(user.getId());
            activity.setCreateTime(DateFormat.formatYMDHms(new Date()));
            activity.setCreateBy(user.getId());
            for (int j=0;j<row.getLastCellNum();j++){
                cell = row.getCell(j);//j单元格信息
                String cellValue="";//单元格值
                if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
                    cellValue=cell.getStringCellValue();
                }else if(cell.getCellType()== HSSFCell.CELL_TYPE_BOOLEAN){
                    cellValue=cell.getBooleanCellValue()+"";
                }else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
                    cellValue=cell.getNumericCellValue()+"";
                }else if(cell.getCellType()== HSSFCell.CELL_TYPE_FORMULA){
                    cellValue=cell.getCellFormula()+"";
                }else {
                    cellValue="";
                }
                if(j==0){
                    activity.setName(cellValue);
                }else if(j==1){
                    activity.setStartDate(cellValue);
                }else if(j==2){
                    activity.setEndDate(cellValue);
                }else if(j==3){
                    activity.setCost(cellValue);
                }else if(j==4){
                    activity.setDescription(cellValue);
                }
            }
            activities.add(activity);
        }
        return activities;
    }
}
