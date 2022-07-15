package com.zhh.crm.workbench.mapper;

import com.zhh.crm.workbench.bean.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * 根据id查ActivityRemark
     * @param id
     * @return
     */
    List<ActivityRemark> selectRemarkById(String id);

    /**
     * 添加活动备注
     * @param remark
     * @return
     */
    int insertActivityRemark(ActivityRemark remark);

    /**
     * 删除市场活动备注
     * @param id
     * @return
     */
    int deleteActivityRemark(String id);

    /**
     * 修改市场活动备注
     * @param remark
     * @return
     */
    int updateActivityRemark(ActivityRemark remark);
}
