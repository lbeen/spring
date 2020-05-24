package com.lbeen.spring.sys.service.impl;

import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.common.util.SqlUtil;
import com.lbeen.spring.sys.bean.Database;
import com.lbeen.spring.sys.bean.Table;
import com.lbeen.spring.sys.mapper.DatabaseMapper;
import com.lbeen.spring.sys.mapper.TableMapper;
import com.lbeen.spring.sys.service.DatabaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    @Resource
    private DatabaseMapper databaseMapper;

    @Resource
    private TableMapper tableMapper;

    @Override
    public Page getDbPage(Integer skip, Integer limit, String dbDesc, String dbType, String used) {
        Map<String, Object> param = new HashMap<>();
        CommonUtil.putIfNotBlank(param, "dbDesc", dbDesc);
        CommonUtil.putIfNotBlank(param, "dbType", dbType);
        CommonUtil.putIfNotBlank(param, "used", used);
        return SqlUtil.queryPage(databaseMapper::count, databaseMapper::selectPage, param, skip, limit);
    }

    @Override
    public Database getOneDb(String id) {
        return databaseMapper.selectOne(id);
    }

    @Override
    public void saveDatabase(Database database) {
        database.setDbDesc(database.getIp() + "_" + database.getPort() + "_" + database.getDbName());
        if (StringUtils.isBlank(database.getId())) {
            database.setId(CommonUtil.uuid());
            databaseMapper.insert(database);
        } else {
            databaseMapper.update(database);
        }
    }

    @Override
    public void deleteDatabase(String id) {
        databaseMapper.delete(id);
    }

    @Override
    public List<Database> getUsedMongoDbs() {
        return databaseMapper.getUsedMongoDbs();
    }

    @Override
    public List<Table> getUsedMongoTables() {
        return databaseMapper.getUsedMongoTables();
    }
}
