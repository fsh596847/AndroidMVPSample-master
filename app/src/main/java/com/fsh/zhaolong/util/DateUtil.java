package com.fsh.zhaolong.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HIPAA on 2016/12/24.
 */

public class DateUtil {

  public static String actualTime() {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
    String str = formatter.format(curDate);
    return str;
  }
}
