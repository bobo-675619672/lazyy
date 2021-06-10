package util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String getTwo(String temp) {
        try {
            BigDecimal b = new BigDecimal(temp);
            return b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            // 转换失败
        }
        return temp;
    }

    public static String getHref(String text) {
        // <html><u><a href='xxx'>xxx</a></u></html>
        String href = "";

        String regex = "\\shref='(\\S+)'>";

        Matcher matcher = Pattern.compile(regex).matcher(text);

        if (matcher.find()) {
            href = matcher.group(1);
        }

        return href;
    }

    public static boolean isNumber(String num) {
        if (StringUtils.isEmpty(num)) {
            return false;
        }
        String regex = "\\d+(\\.\\d+)?";
        return num.matches(regex);
    }

//    public static void main(String[] args) {
//        System.out.println(isNumber("1.1.2"));
//        System.out.println(isNumber("0.2"));
//        System.out.println(isNumber("2"));
//    }

}
