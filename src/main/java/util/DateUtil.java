package util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * 日期工具
 */
public class DateUtil {

    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DATE_FULL = "yyyy-MM-dd HH:mm:ss";

    public static String getCurDateStr() {
        return DateTime.now().toString(FORMAT_DATE);
    }

    public static String getCurDateFullStr() {
        return DateTime.now().toString(FORMAT_DATE_FULL);
    }

    public static Date getDate(String dateStr) {
        return DateTime.parse(dateStr, DateTimeFormat.forPattern(FORMAT_DATE)).toDate();
    }

    public static Date getDateFull(String dateStr) {
        return DateTime.parse(dateStr, DateTimeFormat.forPattern(FORMAT_DATE_FULL)).toDate();
    }

    public static boolean isNowBetween(String start, String end) {
        DateTime startTime = DateTime.parse(start, DateTimeFormat.forPattern(FORMAT_DATE_FULL));
        DateTime endTime = DateTime.parse(end, DateTimeFormat.forPattern(FORMAT_DATE_FULL));
        if (startTime.isBeforeNow() && endTime.isAfterNow()) {
            return true;
        }
        return false;
    }

}
