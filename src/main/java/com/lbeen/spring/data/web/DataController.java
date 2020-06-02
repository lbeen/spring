package com.lbeen.spring.data.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lbeen.spring.common.bean.Page;
import com.lbeen.spring.common.bean.Result;
import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.common.util.MongoUtil;
import com.lbeen.spring.data.util.DataImporter;
import com.lbeen.spring.data.util.DataUtil;
import com.lbeen.spring.sys.bean.Table;
import com.lbeen.spring.sys.bean.TableColumn;
import com.lbeen.spring.sys.service.TableService;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("/data/")
public class DataController {
    private final String uploadTmpPath = "E:/uploadTmpPath/";

    private final static Map<String, DataImporter> CACHE = new HashMap<>();

    @Autowired
    private TableService tableService;

    @RequestMapping("upload")
    public Object upload(@RequestParam("file") MultipartFile srcFile) throws Exception {
        String filename = srcFile.getOriginalFilename();
        String tmpFileName = CommonUtil.uuid() + "." + StringUtils.substringAfterLast(filename, ".");
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

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(uploadTmpPath + tmpFileName), StandardCharsets.UTF_8))) {
            List<TableColumn> columns = tableService.selectColumnsByTableId(tableId);

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

    @RequestMapping("saveData")
    public Object saveData(@RequestBody JSONObject json) {
        String tmpFileName = json.getString("tmpFileName");
        String tableId = json.getString("tableId");
        String split = json.getString("split");
        @SuppressWarnings("unchecked")
        Map<String, Integer> heads = (Map<String, Integer>) json.get("heads");

        Table table = tableService.selectById(tableId);
        if (table == null) {
            return Result.error("表不存在");
        }

        List<BiConsumer<Document, String[]>> valuePuts = DataUtil.getValuePuts(tableId, heads);

        DataImporter dataImporter = new DataImporter(new File(uploadTmpPath + tmpFileName), split, table.getTableName(), valuePuts);
        dataImporter.importData();

        String uuid = CommonUtil.uuid();
        CACHE.put(uuid, dataImporter);
        return Result.success(uuid);
    }

    @RequestMapping("getImportStatus")
    public Object getImportStatus(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            return Result.error("获取导入状态失败");
        }
        DataImporter dataImporter = CACHE.get(uuid);
        if (dataImporter == null) {
            return Result.error("获取导入状态失败");
        }
        Map<String, Object> status = new HashMap<>();
        status.put("total", dataImporter.getTotal());
        status.put("imported", dataImporter.getImported());
        status.put("finish", dataImporter.isFinish());
        if (dataImporter.isFinish()) {
            CACHE.remove(uuid);
        }
        return Result.success(status);
    }

    @RequestMapping("getData")
    public Page getData(String table, Integer skip, Integer limit, String conditions) {
        if (StringUtils.isBlank(table)) {
            return new Page(skip, limit).empty();
        }

        BasicDBObject condition = new BasicDBObject();
        JSONArray jsonArray = JSON.parseArray(conditions);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            String columnName = json.getString("columnName");
            String columnType = json.getString("columnType");
            if (StringUtils.isBlank(columnName) || StringUtils.isBlank(columnType)) {
                continue;
            }
            Object value;
            switch (columnType) {
                case "int":
                    value = json.getInteger("columnValue");
                    break;
                case "long":
                    value = json.getLong("columnValue");
                    break;
                case "double":
                    value = json.getDouble("columnValue");
                    break;
                default:
                    value = json.get("columnValue");
            }
            if (value != null) {
                condition.append(columnName, value);
            }
        }
        return MongoUtil.findPage(table, condition, skip, limit);

    }
}
