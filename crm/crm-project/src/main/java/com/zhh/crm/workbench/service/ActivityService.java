package com.zhh.crm.workbench.service;

import com.zhh.crm.workbench.bean.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    /**
     *创建Activity
     */
    int saveActivity(Activity activity);
    /**
     * 分页条件查询
     * @return
     */
    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);
    /**
     * 查询总条数
     * @param map
     * @return
     */
    int selectCount(Map<String,Object> map);
    /**
     * 根据id删除Activity
     * @param id
     * @return
     */
    int deleteActivityById(String [] id);
    /**
     * 根据id查询Activity
     * @param id
     * @return
     */
    Activity selectById(String id);
    /**
     * 根据id修改activity
     * @param activity
     * @return
     */
    int updateActivityById(Activity activity);
    /**
     * 查询所有Activity
     * @return
     */
    List<Activity> selectAllActivity();
    /**
     * 根据多个id查询activity
     * @param id
     * @return
     */
    List<Activity> selectByIds(String [] id);
    /**
     * 批量插入Activity
     * @param activities
     * @return
     */
    int insertActivityBatch(List<Activity> activities);
    /**
     * 通过id查询activity,需要连接查询到真正的姓名
     * @param id
     * @return
     */
    Activity selectActivityById(String id);

}
