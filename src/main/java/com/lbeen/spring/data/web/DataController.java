package com.lbeen.spring.data.web;

import com.alibaba.fastjson.JSONObject;
import com.lbeen.spring.common.util.MongoUtil;
import com.lbeen.spring.common.util.R;
import com.lbeen.spring.common.web.Result;
import com.lbeen.spring.constants.MongoTable;
import com.lbeen.spring.database.bean.Table;
import com.lbeen.spring.database.service.DatabaseService;
import com.mongodb.BasicDBObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/data/")
public class DataController {
    private final String uploadTmpPath = "E:/uploadTmpPath/";

    @Autowired
    private DatabaseService databaseService;

    @RequestMapping("upload")
    public Object upload(@RequestParam("file") MultipartFile srcFile) throws Exception {
        String filename = srcFile.getOriginalFilename();
        String tmpFileName = R.uuid() + "." + StringUtils.substringAfterLast(filename, ".");
        try (InputStream is = srcFile.getInputStream();
             FileOutputStream os = new FileOutputStream(uploadTmpPath + tmpFileName)
        ) {
            IOUtils.copy(is, os);
            Map<String, String> data = new HashMap<>();
            data.put("fileName", filename);
            data.put("tmpFileName", tmpFileName);
            return Result.success(data);
        }
    }

    @RequestMapping("preview")
    public Object preview(String tmpFileName, String tableId, String split, Integer previewCount) throws Exception {
        if (previewCount == null) {
            previewCount = 50;
        }
        split = getSplit(split);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(uploadTmpPath + tmpFileName), StandardCharsets.UTF_8))) {
            List<Document> columns = MongoUtil.find(MongoTable.SYS_TABLE_COLUMN, new BasicDBObject("tableId", tableId));


            int maxLineSize = columns.size();
            List<List<String>> lines = new ArrayList<>();
            for (int i = 0; i < previewCount; i++) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                String[] arr = line.split(split);
                lines.add(Arrays.asList(arr));
                if (arr.length > maxLineSize) {
                    maxLineSize = arr.length;
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("maxLineSize", maxLineSize);
            data.put("lines", lines);
            data.put("columns", columns);
            return Result.success(data);
        }
    }

    private String getSplit(String split) {
        if ("comma".equals(split)) {
            return ",";
        }
        if ("制表符".equals(split)) {
            return "\t";
        }
        return ",";
    }

    @RequestMapping("saveData")
    public Object saveData(@RequestBody JSONObject json) throws Exception {
        String tmpFileName = json.getString("tmpFileName");
        String tableId = json.getString("tableId");
        String split = getSplit(json.getString("split"));
        @SuppressWarnings("unchecked")
        Map<String, Integer> heads = (Map<String, Integer>) json.get("heads");

        Table table = databaseService.getOneTable(tableId);
        if (table == null) {
            return Result.error("表不存在");
        }

        List<Document> inserts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(uploadTmpPath + tmpFileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(split);

                Document document = new Document();
                for (Map.Entry<String, Integer> entry : heads.entrySet()) {
                    if (entry.getValue() >= data.length) {
                        continue;
                    }
                    document.put(entry.getKey(), data[entry.getValue()]);
                }
                if (document.isEmpty()) {
                    continue;
                }
                document.put("_id", R.uuid());
                inserts.add(document);

                if (inserts.size() == 5000) {
                    MongoUtil.insertList(table.getTableName(), inserts);
                    inserts.clear();
                }
            }
            if (!inserts.isEmpty()) {
                MongoUtil.insertList(table.getTableName(), inserts);
                inserts.clear();
            }
            return Result.success();
        }
    }
}
