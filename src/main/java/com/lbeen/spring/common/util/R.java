package com.lbeen.spring.common.util;

        import java.util.UUID;

public class R {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
