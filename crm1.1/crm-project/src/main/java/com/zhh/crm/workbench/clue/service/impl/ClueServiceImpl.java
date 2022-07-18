package com.zhh.crm.workbench.clue.service.impl;

import com.zhh.crm.workbench.clue.bean.Clue;
import com.zhh.crm.workbench.clue.mapper.ClueMapper;
import com.zhh.crm.workbench.clue.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Override
    public int saveClue(Clue clue) {
        return clueMapper.saveClue(clue);
    }

    @Override
    public List<Clue> selectClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int selectClueAccount(Map<String, Object> map) {
        return clueMapper.selectClueAccount(map);
    }
}
