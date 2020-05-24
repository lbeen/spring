package com.lbeen.spring.sys.web;

import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.sys.bean.Database;
import com.lbeen.spring.sys.service.DatabaseService;
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
}
