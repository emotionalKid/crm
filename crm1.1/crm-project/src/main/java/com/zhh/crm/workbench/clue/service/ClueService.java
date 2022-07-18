package com.zhh.crm.workbench.clue.service;

import com.zhh.crm.workbench.clue.bean.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    /**
     * 插入clue
     * @param clue
     * @return
     */
    int saveClue(Clue clue);
    /**
     * 根据条件分页查询
     * @param map
     * @return
     */
    List<Clue> selectClueByConditionForPage(Map<String,Object> map);
    /**
     * 查询clue总条数
     * @return
     */
    int selectClueAccount(Map<String,Object> map);
}
