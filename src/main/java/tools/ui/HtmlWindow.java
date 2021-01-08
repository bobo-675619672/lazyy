package tools.ui;

import constant.LazyyConstant;
import util.LogUtil;

import javax.swing.*;
import java.net.URL;

public class HtmlWindow {
    private JEditorPane htmlEditorPane;
    private JPanel mPanel;

    public JPanel getHtmlPanel() {
        try {
            htmlEditorPane.setPage("file://html/chart.html");
            LogUtil.info("html加载成功!");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info("html加载失败!");
        }

        return mPanel;
    }

}
