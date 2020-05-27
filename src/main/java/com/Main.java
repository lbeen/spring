package com;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) throws Exception {

        AtomicLong imported= new AtomicLong(0);

        imported.addAndGet(50);
        System.out.println(imported);
        imported.addAndGet(50);

        System.out.println(imported);


    }
}
