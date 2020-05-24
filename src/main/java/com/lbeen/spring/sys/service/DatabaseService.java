package com.lbeen.spring.sys.service;

import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.sys.bean.Database;
import com.lbeen.spring.sys.bean.Table;

import java.util.List;

public interface DatabaseService {
    Page getDbPage(Integer skip, Integer limit, String dbDesc, String dbType, String used);

    Database getOneDb(String id);

    void saveDatabase(Database database);

    void deleteDatabase(String id);

    List<Database> getUsedMongoDbs();

    List<Table> getUsedMongoTables();
}
