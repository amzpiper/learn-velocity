package com.excel.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class T1 {
    public static void main(String[] args) {
        System.out.println((int)Math.ceil(0.901));
        int x =  10;
        for(int i = 0; i < 30; i++) {
            int day = i + 1;
            String dayStr = day + "";
            if (day < 10) {
                dayStr = "0" + day;
            }
            String s = "{\n" +
                    "      \"VALUE\": \"" + x  + "\",\n" +
                    "      \"COLLECTTIME\": \"2020-06-" + dayStr+ " 00:00:00\"\n" +
                    "    },";
            x = g();
            //System.out.println(s);
        }
    }

    public static int g() {
        int max=20;

        int min=10;

        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
}
