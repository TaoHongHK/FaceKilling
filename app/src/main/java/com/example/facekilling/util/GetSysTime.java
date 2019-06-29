package com.example.facekilling.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetSysTime {
    public static String getCurrTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
        return simpleDateFormat.format(new Date());
    }
}
