package com.freight.contract.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/2/9.
 */
public class TimeFormatUtil implements Serializable {

    private Pattern P_YMD = Pattern.compile("\\d{4}[-/\\.]\\d{1,2}[-/\\.]\\d{1,2}");
    private Pattern P_YMDHMS = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}");
    private Pattern P_YMDHMS2 = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2} \\d{1,2} \\d{1,2}");
    private Pattern P_YMDHM = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}");
    private Pattern P_MDYHMS = Pattern.compile("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}");
    //2016-12-26 16:14:24.0
    private Pattern P_YMDHMSS = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.*");
    /**
     * 不带格式的年
     */
    private DateFormat SDF_Y = new SimpleDateFormat("yyyy");
    /**
     * 不带格式的年月
     */
    private DateFormat SDF_YM = new SimpleDateFormat("yyyyMM");
    /**
     * 不带格式的年月日
     */
    private DateFormat SDF_YMD = new SimpleDateFormat("yyyyMMdd");
    /**
     * 不带格式的年月日时
     */
    private DateFormat SDF_YMDH = new SimpleDateFormat("yyyyMMddHH");
    /**
     * 不带格式的年月日时分
     */
    private DateFormat SDF_YMDHM = new SimpleDateFormat("yyyyMMddHHmm");
    /**
     * 不带格式的年月日时分秒 yyyyMMddHHmmss
     */
    private DateFormat SDF_YMDHMS = new SimpleDateFormat("yyyyMMddHHmmss");


    private DateFormat SDF_YMDHMSFFF = new SimpleDateFormat("yyyyMMddHHmmssFFF");
    /**
     * 带格式的年月日时分秒
     */
    private DateFormat FMT_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat FMT_YMDHMS2 = new SimpleDateFormat("yyyy-MM-dd HH mm ss");
    /**
     * 带格式的年月日时分秒
     */
    private DateFormat FMT_MDYHMS = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    /**
     * 带格式的年月日
     */
    private DateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat FMT_YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private DateFormat FMT_YMD1 = new SimpleDateFormat("yyyy.MM.dd");
    private DateFormat FMT_YMD2 = new SimpleDateFormat("yyyy\\MM\\dd");
    private DateFormat FMT_YMD3 = new SimpleDateFormat("yyyy/MM/dd");


    /**
     * 日期转换
     */
    public Date convert(String date) {
        Date d = null;
        try {
            if (P_YMD.matcher(date).matches()) {
                try {
                    d = FMT_YMD.parse(date);
                } catch (Exception e1) {
                    try {
                        d = FMT_YMD1.parse(date);
                    } catch (Exception e2) {
                        try {
                            d = FMT_YMD2.parse(date);
                        } catch (Exception e3) {
                            d = FMT_YMD3.parse(date);
                        }
                    }
                }
            } else if (P_YMDHMS.matcher(date).matches()) {
                d = FMT_YMDHMS.parse(date);

            } else if (P_YMDHMS2.matcher(date).matches()) {
                d = FMT_YMDHMS2.parse(date);

            } else if (P_YMDHM.matcher(date).matches()) {
                d = FMT_YMDHM.parse(date);

            } else if (P_MDYHMS.matcher(date).matches()) {
                d = FMT_MDYHMS.parse(date);

            } else if (P_YMDHMSS.matcher(date).matches()) {
                date = date.split("\\.")[0];
                d = FMT_YMDHMS.parse(date);

            } else if (date.length() == 8) {
                d = SDF_YMD.parse(date);

            } else if (date.length() == 12) {
                d = SDF_YMDHM.parse(date);

            } else if (date.length() == 14) {
                d = SDF_YMDHMS.parse(date);
            } else if (date.length() == 17) {
                d = SDF_YMDHMSFFF.parse(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }


    public Date str2Date(String date) {
        if (date == null)
            return null;
        try {
            if (P_YMD.matcher(date).matches()) {
                Date d = null;
                try {
                    d = FMT_YMD.parse(date);
                } catch (Exception e1) {
                    try {
                        d = FMT_YMD1.parse(date);
                    } catch (Exception e2) {
                        try {
                            d = FMT_YMD2.parse(date);
                        } catch (Exception e3) {
                            d = FMT_YMD3.parse(date);
                        }
                    }
                }
                return d;
            } else if (P_YMDHMS.matcher(date).matches()) {
                return FMT_YMDHMS.parse(date);
            } else if (P_YMDHMS2.matcher(date).matches()) {
                return FMT_YMDHMS2.parse(date);
            } else if (P_MDYHMS.matcher(date).matches()) {
                return FMT_MDYHMS.parse(date);
            } else if (P_YMDHMSS.matcher(date).matches()) {
                date = date.split("\\.")[0];
                return FMT_YMDHMS.parse(date);
            } else if (date.length() == 8) {
                return SDF_YMD.parse(date);
            } else if (date.length() == 12) {
                return SDF_YMDHM.parse(date);
            } else if (date.length() == 14) {
                return SDF_YMDHMS.parse(date);
            } else if (date.length() == 17) {
                return SDF_YMDHMSFFF.parse(date);
            }
        } catch (Exception e) {
        }
        return null;
    }


    public static void main(String[] args) throws ParseException {

        String dateStr = "20160410125000000";

//        System.out.println(new TimeFormatUtil().convert(dateStr));

//        DateFormat FMT_YMD = new SimpleDateFormat("yyyy-MM-dd");
//        DateFormat FMT_YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        DateFormat FMT_YMD1 = new SimpleDateFormat("yyyy.MM.dd");
//        DateFormat FMT_YMD2 = new SimpleDateFormat("yyyy\\MM\\dd");
//        DateFormat FMT_YMD3 = new SimpleDateFormat("yyyy/MM/dd");
//        String date = "1999-5-1";
//        Pattern P_YMD = Pattern.compile("\\d{4}[-/\\.]\\d{1,2}[-/\\.]\\d{1,2}");
//        System.out.println(P_YMD.matcher("1999-5-1").matches());
//        Date d;
//        try {
//            d = FMT_YMD.parse(date);
//        } catch (Exception e1) {
//            try {
//                d = FMT_YMD1.parse(date);
//            } catch (Exception e2) {
//                try {
//                    d = FMT_YMD2.parse(date);
//                } catch (Exception e3) {
//                    d = FMT_YMD3.parse(date);
//                }
//            }
//        }
//        System.out.println(d);
    }
}
