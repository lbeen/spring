package com.lbeen.spring.database.mapper;

import com.lbeen.spring.database.bean.Database;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DatabaseMapper {
    int count(Database database);

    List<Database> selectPage(Database database);

    Database selectOne(String id);

    void insertDatabase(Database database);

    void updateDatabase(Database database);

    void deleteDatabase(String id);
}
