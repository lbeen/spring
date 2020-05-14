package com.lbeen.spring.database.service;

import com.lbeen.spring.common.page.Page;
import com.lbeen.spring.database.bean.Database;

public interface DatabaseService {
    Page getPage(Database database);

    Database getOne(String id);

    void saveDatabase(Database database);

    void deleteDatabase(String id);
}
