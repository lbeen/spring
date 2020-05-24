package com.lbeen.spring.sys.service;

import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.sys.bean.Dic;

import java.util.List;

public interface DicService {
    Result saveDic(Dic dic);

    Dic selectById(String id);

    List<Dic> selectDicByParentId(String parentId);

    List<Dic> selectAllUsedDic();
}
