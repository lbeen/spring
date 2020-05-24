package com.lbeen.spring.sys.mapper;

import com.lbeen.spring.sys.bean.Dic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DicMapper {
    void insert(Dic dic);

    void update(Dic dic);

    Dic selectById(String id);

    Dic selectOne(Dic dic);

    List<Dic> selectDicByParentId(String parentId);

    List<Dic> selectAllUsedDic();
}
