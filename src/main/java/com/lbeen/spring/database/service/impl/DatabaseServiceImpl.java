package com.lbeen.spring.database.service.impl;

import com.lbeen.spring.common.page.Page;
import com.lbeen.spring.common.page.PageUtil;
import com.lbeen.spring.common.util.R;
import com.lbeen.spring.database.bean.Database;
import com.lbeen.spring.database.mapper.DatabaseMapper;
import com.lbeen.spring.database.service.DatabaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    @Resource
    private DatabaseMapper databaseMapper;

    @Override
    public Page getPage(Database database) {
        return PageUtil.getPage(() -> databaseMapper.count(database), () -> databaseMapper.selectPage(database));
    }

    @Override
    public Database getOne(String id) {
        return databaseMapper.selectOne(id);
    }

    @Override
    public void saveDatabase(Database database) {
        database.setDbDesc(database.getIp() + "_" + database.getPort() + "_" + database.getDbName());
        if (StringUtils.isBlank(database.getId())) {
            database.setId(R.uuid());
            databaseMapper.insertDatabase(database);
        } else {
            databaseMapper.updateDatabase(database);
        }
    }

    @Override
    public void deleteDatabase(String id){
        databaseMapper.deleteDatabase(id);
    }
}
