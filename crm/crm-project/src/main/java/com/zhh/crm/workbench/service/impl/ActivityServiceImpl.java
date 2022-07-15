package com.zhh.crm.workbench.service.impl;

import com.zhh.crm.workbench.bean.Activity;
import com.zhh.crm.workbench.mapper.ActivityMapper;
import com.zhh.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    /**
     *创建Activity
     */
    @Override
    public int saveActivity(Activity activity) {
        return activityMapper.saveActivity(activity);
    }

    @Override
    public List<Activity> selectActivityByConditionForPage(Map<String ,Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int selectCount(Map<String, Object> map) {
        return activityMapper.selectCount(map);
    }

    @Override
    public int deleteActivityById(String[] id) {
        return activityMapper.deleteActivityById(id);
    }

    @Override
    public Activity selectById(String id) {
        return activityMapper.selectById(id);
    }

    @Override
    public int updateActivityById(Activity activity) {
        return activityMapper.updateActivityById(activity);
    }

    @Override
    public List<Activity> selectAllActivity() {
        return activityMapper.selectAllActivity();
    }

    @Override
    public List<Activity> selectByIds(String[] id) {
        return activityMapper.selectByIds(id);
    }

    @Override
    public int insertActivityBatch(List<Activity> activities) {
        return activityMapper.insertActivityBatch(activities);
    }

    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

}
