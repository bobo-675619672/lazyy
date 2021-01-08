package tools.ui;

import com.intellij.ui.content.ContentFactory;
import constant.LazyyConstant;
import util.LogUtil;
import util.thread.CommonThreadPool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

public class ShowWindow {

    private JPanel sPanel;
    private JLabel sLabel;
    private JButton refreshButton;
    private JLabel logoLabel;
    private JComboBox kanComboBox;

    private CommonThreadPool commonThreadPool = CommonThreadPool.getInstance();

    public JPanel getShowPanel() {
        logoLabel.setText(LazyyConstant.LABEL_LOGO);
        refreshButton.addActionListener(a -> {
            this.onInit();
        });
        // 添加切换事件
        kanComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.onInit();
            }
        });
        return sPanel;
    }

    public void onInit() {
        // 线程执行
        commonThreadPool.execute(() -> {
            int index = kanComboBox.getSelectedIndex();
            String name = kanComboBox.getSelectedItem().toString();
            try {
                if (index >= LazyyConstant.KKP_CODES.length) {
                    LogUtil.info("数据异常,不刷新!");
                    return;
                }
                ImageIcon background = new ImageIcon(ImageIO.read(new URL("http://image.sinajs.cn/newchart/min/n/" + LazyyConstant.KKP_CODES[index] + ".gif")));
                sLabel.setIcon(background);
                LogUtil.info(name + "数据更新成功!");
            } catch (Exception e) {
                LogUtil.info(name + "数据更新失败!");
            }
        });
    }

}
