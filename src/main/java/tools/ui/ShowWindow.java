package tools.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import constant.LazyyConstant;
import org.jetbrains.annotations.NotNull;
import util.LogUtil;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ShowWindow {

    private JPanel sPanel;
    private JLabel sLabel;
    private JButton refreshButton;
    private JLabel logoLabel;
    private JComboBox kanComboBox;

    public JPanel getShowPanel() {
        logoLabel.setText(LazyyConstant.LABEL_LOGO);
        refreshButton.addActionListener(a -> {
            this.onInit();
        });
        return sPanel;
    }

    public void onInit() {
        int index = kanComboBox.getSelectedIndex();
        String name = kanComboBox.getSelectedItem().toString();
        try {
            if (index >= LazyyConstant.KKP_URLS.length) {
                LogUtil.info("数据异常,不刷新!");
                return;
            }
            ImageIcon background = new ImageIcon(new URL(LazyyConstant.KKP_URLS[index]));
            sLabel.setIcon(background);
            LogUtil.info(name + "数据更新成功!");
        } catch (Exception e) {
            LogUtil.info(name + "数据更新失败!");
        }

    }

}
