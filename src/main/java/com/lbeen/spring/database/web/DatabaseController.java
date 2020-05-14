package com.lbeen.spring.database.web;

import com.lbeen.spring.common.page.Page;
import com.lbeen.spring.common.web.Result;
import com.lbeen.spring.database.bean.Database;
import com.lbeen.spring.database.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database/")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @RequestMapping("getPage")
    public Page getPage(Database database, Integer pageSize, Integer currentPage) {
        database.setPagePage(pageSize, currentPage);
        return databaseService.getPage(database);
    }

    @RequestMapping("getOne")
    public Database getOne(String id) {
        return databaseService.getOne(id);
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
