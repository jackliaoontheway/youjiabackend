package com.polarj.common;

import java.text.SimpleDateFormat;

public final class CommonConstant
{
    final public static String DateTimeFormatString = "yyyy-MM-dd HH:mm:ss";

    final public static SimpleDateFormat DateTimeFormat = new SimpleDateFormat(DateTimeFormatString);

    final public static String DateTimeFormatForFileNameString = "yyyyMMddHHmmss";

    final public static SimpleDateFormat DateTimeFormatForFileName =
            new SimpleDateFormat(DateTimeFormatForFileNameString);

    final public static String DateTimeFormatWithMicroSecondForFileNameString = "yyyyMMddHHmmssSSS";

    final public static SimpleDateFormat DateTimeFormatWithMicroSecondForFileName =
            new SimpleDateFormat(DateTimeFormatWithMicroSecondForFileNameString);

    final public static String DateFormatString = "yyyy-MM-dd";

    final public static SimpleDateFormat DateFormat = new SimpleDateFormat(DateFormatString);

    final public static String DateFormatSelectString = "yyyy/MM/dd";

    final public static SimpleDateFormat DateFormatSelect = new SimpleDateFormat(DateFormatSelectString);

    final public static String DateHMFormatString = "yyyy-MM-dd HH:mm";

    final public static SimpleDateFormat DateHMFormat = new SimpleDateFormat(DateHMFormatString);

    final public static SimpleDateFormat DateFormatForFile = new SimpleDateFormat("yyyyMMdd");

    final public static String ShortDateFormatString = "MMM-dd-yy";

    final public static SimpleDateFormat ShortDateFormat = new SimpleDateFormat(ShortDateFormatString);

    final public static String TsvSuffix = ".tsv";

    final public static String CsvSuffix = ".csv";

    final public static String xlsSuffix = ".xls";

    final public static String xlsxSuffix = ".xlsx";

    final public static String defaultSystemLanguage = "en-us";

    // 系统使用的货币，所有的其他显示货币使用（前一天/即时）的汇率转换成系统缺省货币进行内部计算
    // 支持的货币由支持的国家来确定，已经持久化到系统中。
    // 目前系统使用美元进行内部计算
    final public static String defaultCurrencyCode = "USD";

    // 系统自动操作的用户编码，如下情况使用该编码：
    // 1. 初始化数据
    // 2. 系统定时任务产生的数据
    // QUES
    final public static int systemUserAccountId = -1;

    final public static String BlankFileName = "blank.";

    final public static String defaultPassword = "asdasd123";
}
