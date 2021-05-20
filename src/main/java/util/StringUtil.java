package util;

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

//    public static void main(String[] args) {
//        System.out.println(getHref("<html><u><a href='111'>222</a></u></html>"));
//    }

}
