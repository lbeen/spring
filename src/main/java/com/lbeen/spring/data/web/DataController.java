package com.lbeen.spring.data.web;

import com.lbeen.spring.common.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/data/")
public class DataController {



    @RequestMapping("test")
    public Object test() {


        return MongoUtil.find("test",new BasicDBObject(),1 , 2);
    }
}
