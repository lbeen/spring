package com.lbeen.spring.database.mapper;

import com.lbeen.spring.database.bean.Database;
import com.lbeen.spring.database.bean.Table;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DatabaseMapper {
    int count(Map<String, Object> param);

    List<Database> selectPage(Map<String, Object> param);

    Database selectOne(String id);

    void insert(Database database);

    void update(Database database);

    void delete(String id);

    List<Database> getUsedMongoDbs();

    List<Table> getUsedMongoTables();
}
