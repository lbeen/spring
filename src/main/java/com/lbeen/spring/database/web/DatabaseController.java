package com.lbeen.spring.database.web;

import com.alibaba.fastjson.JSONObject;
import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.common.util.MongoUtil;
import com.lbeen.spring.common.util.R;
import com.lbeen.spring.common.web.Result;
import com.lbeen.spring.constants.MongoTable;
import com.lbeen.spring.database.bean.Database;
import com.lbeen.spring.database.bean.Table;
import com.lbeen.spring.database.service.DatabaseService;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
        MongoUtil.delete(MongoTable.SYS_TABLE_COLUMN, new BasicDBObject("_id", id));
        return Result.success();
    }

    @RequestMapping("refreshTableCatch")
    public Result refreshTableCatch() {
        MongoUtil.loadCache();
        return Result.success();
    }


    @RequestMapping("getTableColumns")
    public List<Document> getTableColumns(String tableId) {
        return MongoUtil.find(MongoTable.SYS_TABLE_COLUMN, new BasicDBObject("tableId", tableId));
    }

    @RequestMapping("saveTableColumns")
    public Result saveTableColumns(@RequestBody JSONObject json) {
        String tableId = json.getString("tableId");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tableColumns = (List<Map<String, Object>>) json.get("tableColumns");

        List<Document> inserts = CommonUtil.mapList2Documents(tableColumns);
        for (Document document : inserts) {
            document.put("_id", R.uuid());
            document.put("tableId", tableId);
        }
        MongoUtil.delete(MongoTable.SYS_TABLE_COLUMN, new BasicDBObject("tableId", tableId));
        MongoUtil.insertList(MongoTable.SYS_TABLE_COLUMN, inserts);

        return Result.success();
    }
}
