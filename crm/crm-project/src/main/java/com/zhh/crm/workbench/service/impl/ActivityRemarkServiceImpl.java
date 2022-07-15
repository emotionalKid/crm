package com.zhh.crm.workbench.service.impl;

import com.zhh.crm.workbench.bean.ActivityRemark;
import com.zhh.crm.workbench.mapper.ActivityMapper;
import com.zhh.crm.workbench.mapper.ActivityRemarkMapper;
import com.zhh.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;
    @Override
    public List<ActivityRemark> selectRemarkById(String id) {
        return activityRemarkMapper.selectRemarkById(id);
    }

    @Override
    public int insertActivityRemark(ActivityRemark remark) {
        return activityRemarkMapper.insertActivityRemark(remark);
    }

    @Override
    public int deleteActivityRemark(String id) {
        return activityRemarkMapper.deleteActivityRemark(id);
    }

    @Override
    public int updateActivityRemark(ActivityRemark remark) {
        return activityRemarkMapper.updateActivityRemark(remark);
    }
}
