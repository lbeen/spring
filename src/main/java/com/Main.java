package com;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) throws Exception {
//        str


//        System.out.println(Integer.parseInt("107780"));
//        System.out.println(Integer.parseInt("107780"));
//        System.out.println("﻿107780".length());
        System.out.println(Arrays.toString("107780".getBytes()));
//
//        System.out.println("107780".length());
//
        System.out.println(Arrays.toString("﻿107780".getBytes(StandardCharsets.UTF_8)));
        byte[] bytes = "﻿107780".getBytes(StandardCharsets.UTF_8);
        if (bytes.length >= 3 && bytes[0] == -17 && bytes[1] == -69 && bytes[2] == -65) {
            System.out.println(1);
            byte[] dest = new byte[bytes.length - 3];
            System.arraycopy(bytes, 3, dest, 0, dest.length);
            System.out.println(new String(dest, StandardCharsets.UTF_8));
        }

////        System.out.println(Integer.parseInt("﻿107780"));
//        System.out.println(new String("﻿107780".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
//
//        System.out.println(Long.parseLong("﻿7L"));


    }
}
