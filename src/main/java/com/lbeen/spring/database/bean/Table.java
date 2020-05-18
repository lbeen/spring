package com.lbeen.spring.database.bean;

/**
 * CREATE TABLE `t_table` (
 *   `id` varchar(32) NOT NULL,
 *   `dbId` varchar(32) DEFAULT NULL,
 *   `tableName` varchar(20) DEFAULT NULL,
 *   `tableDesc` varchar(20) DEFAULT NULL,
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
public class Table {
    private String id;
    private String dbId;
    private String tableName;
    private String tableDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }
}
