package com.lbeen.spring.database.service;

import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.database.bean.Database;
import com.lbeen.spring.database.bean.Table;

import java.util.List;

public interface DatabaseService {
    Page getDbPage(Integer skip, Integer limit, String dbDesc, String dbType, String used);

    Database getOneDb(String id);

    void saveDatabase(Database database);

    void deleteDatabase(String id);

    Page getTablePage(Integer skip, Integer limit, String dbId, String tableName, String tableDesc);

    Table getOneTable(String id);

    void saveTable(Table table);

    void deleteTable(String id);

    List<Database> getUsedMongoDbs();

    List<Table> getUsedMongoTables();
}
