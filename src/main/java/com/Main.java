package com;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        FileReader in = new FileReader(new File("E:\\数据资源\\用户贷款数据\\个人征信\\train\\browse_history_train.txt"));
        LineNumberReader reader = new LineNumberReader(in);
        System.out.println(reader.skip(Long.MAX_VALUE));
        int lines = reader.getLineNumber();
        reader.close();
        long endTime = System.currentTimeMillis();

        System.out.println(lines);

        System.out.println("统计文件行数运行时间： " + (endTime - startTime) + "ms");


    }
}
