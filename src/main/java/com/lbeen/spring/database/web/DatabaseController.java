package com.lbeen.spring.database.web;

import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.web.Result;
import com.lbeen.spring.database.bean.Database;
import com.lbeen.spring.database.bean.Table;
import com.lbeen.spring.database.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database/")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @RequestMapping("getDbPage")
    public Page getDbPage(Integer skip, Integer limit, String dbDesc, String dbType, String used) {
        return databaseService.getDbPage(skip, limit, dbDesc, dbType, used);
    }

    @RequestMapping("getOneDb")
    public Database getOneDb(String id) {
        return databaseService.getOneDb(id);
    }

    @RequestMapping("saveDatabase")
    public Result saveDatabase(Database database) {
        databaseService.saveDatabase(database);
        return Result.success();
    }

    @RequestMapping("deleteDatabase")
    public Result deleteDatabase(String id) {
        databaseService.deleteDatabase(id);
        return Result.success();
    }

    @RequestMapping("getTablePage")
    public Page getTablePage(Integer skip, Integer limit, String dbId, String tableName, String tableDesc) {
        return databaseService.getTablePage(skip, limit, dbId, tableName, tableDesc);
    }

    @RequestMapping("getOneTable")
    public Table getOneTable(String id) {
        return databaseService.getOneTable(id);
    }

    @RequestMapping("saveTable")
    public Result saveTable(Table table) {
        databaseService.saveTable(table);
        return Result.success();
    }

    @RequestMapping("deleteTable")
    public Result deleteTable(String id) {
        databaseService.deleteTable(id);
        return Result.success();
    }
}
