package tools.ui;

import constant.LazyyConstant;
import util.LogUtil;
import util.thread.CommonThreadPool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.net.URL;

public class ShowWindow {

    private JPanel sPanel;
    private JLabel sLabel;
    private JButton refreshButton;
    private JLabel logoLabel;
    private JComboBox codeComboBox;
    private JComboBox typeComboBox;

    private CommonThreadPool commonThreadPool = CommonThreadPool.getInstance();

    public JPanel getShowPanel() {
        logoLabel.setText(LazyyConstant.LABEL_LOGO);
        refreshButton.addActionListener(a -> {
            this.onInit();
        });
        // 添加切换事件
        codeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.onInit();
            }
        });
        typeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.onInit();
            }
        });
        return sPanel;
    }

    public void onInit() {
        // 线程执行
        commonThreadPool.execute(() -> {
            int codeIndex = codeComboBox.getSelectedIndex();
            String code = codeComboBox.getSelectedItem().toString();
            int typeIndex = typeComboBox.getSelectedIndex();
            String type = typeComboBox.getSelectedItem().toString();
            if (codeIndex >= LazyyConstant.KKP_CODES.length || typeIndex >= LazyyConstant.KKP_TYPES.length) {
                LogUtil.info("数据异常,不刷新!");
                return;
            }
            String url = "http://image.sinajs.cn/newchart/" + LazyyConstant.KKP_TYPES[typeIndex] + "/n/" + LazyyConstant.KKP_CODES[codeIndex] + ".gif";
            try {
                ImageIcon background = new ImageIcon(ImageIO.read(new URL(url)));
                sLabel.setIcon(background);
                LogUtil.info(code + " " + type + " 数据更新成功!");
            } catch (Exception e) {
                LogUtil.info(code + " " + type + " 数据更新失败! -> URL: " + url);
            }
        });
    }

}
