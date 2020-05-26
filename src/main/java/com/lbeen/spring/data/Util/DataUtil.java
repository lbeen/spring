package com.lbeen.spring.data.Util;

import com.lbeen.spring.common.util.CommonUtil;
import com.lbeen.spring.common.util.SpringUtil;
import com.lbeen.spring.sys.bean.TableColumn;
import com.lbeen.spring.sys.service.TableService;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DataUtil {
    public static List<BiConsumer<Document, String[]>> getValuePuts(String tableId, Map<String, Integer> heads) {
        TableService tableService = SpringUtil.getBean("tableServiceImpl");
        List<TableColumn> columns = tableService.selectColumnsByTableId(tableId);
        Map<String, String> columnsType = columns.stream().collect(Collectors.toMap(TableColumn::getColumnName, TableColumn::getColumnType));
        List<BiConsumer<Document, String[]>> valuePuts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : heads.entrySet()) {
            BiConsumer<Document, String[]> valuePut;
            switch (columnsType.get(entry.getKey())) {
                case "long":
                    valuePut = (doc, data) -> {
                        if (entry.getValue() < data.length) {
                            doc.put(entry.getKey(), CommonUtil.toLong(data[entry.getValue()]));
                        }
                    };
                    break;
                case "int":
                    valuePut = (doc, data) -> {
                        if (entry.getValue() < data.length) {
                            doc.put(entry.getKey(), CommonUtil.toInt(data[entry.getValue()]));
                        }
                    };
                    break;
                case "double":
                    valuePut = (doc, data) -> {
                        if (entry.getValue() < data.length) {
                            doc.put(entry.getKey(), CommonUtil.toDouble(data[entry.getValue()]));
                        }
                    };
                    break;
                default:
                    valuePut = (doc, data) -> {
                        if (entry.getValue() < data.length) {
                            doc.put(entry.getKey(), data[entry.getValue()]);
                        }
                    };
                    break;
            }
            valuePuts.add(valuePut);
        }
        return valuePuts;
    }
}
