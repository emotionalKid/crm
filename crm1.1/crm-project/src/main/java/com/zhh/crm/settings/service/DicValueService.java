package com.zhh.crm.settings.service;

import com.zhh.crm.settings.bean.DicValue;

import java.util.List;

public interface DicValueService {
    /**
     * 根据typeCode查询字典值
     * @param typeCode
     * @return
     */
    List<DicValue> selectDicValueByTypeCode(String typeCode);
}
