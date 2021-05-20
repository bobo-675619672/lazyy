package tools.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.components.ServiceManager;
import constant.LazyyConstant;
import model.NewsBean;
import storage.LazyyHelperSettings;
import util.HttpClientPool;
import util.LogUtil;
import util.StringUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class NewsWindow {

    private JPanel sPanel;
    private JTable newsTable;
    private JLabel logoLabel;
    private JButton refreshButton;

    private int[] sizes = new int[]{400, 40};
    private static Gson gson = new Gson();

    private LazyyHelperSettings settings;

    public JPanel getNewsPanel() {
        this.settings = ServiceManager.getService(LazyyHelperSettings.class);
        logoLabel.setText(LazyyConstant.LABEL_LOGO);
        refreshButton.addActionListener(a -> {
            this.onInit();
        });
        // 悬浮提示单元格的值
        newsTable.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int column = newsTable.columnAtPoint(e.getPoint());
                if (0 == column) {
                    // 悬浮显示单元格内容
                    newsTable.setToolTipText("双击标题,浏览器打开阅读文章...");
                }
            }
        });
        // 添加鼠标点击事件
        newsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 点击几次，这里是双击事件
                if (e.getClickCount() == 2) {
                    int row = newsTable.getSelectedRow();
                    int column = newsTable.getSelectedColumn();
                    if (0 == column) {
                        String text = String.valueOf(newsTable.getValueAt(row, column));
                        String href = StringUtil.getHref(text);
                        try {
                            Desktop.getDesktop().browse(new java.net.URI(href));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        return sPanel;
    }

    public void onInit() {
        String newsCount = settings.getAdvancedSettings().getNewsCount();
        // 读取新闻
        LogUtil.info("刷新新闻!最新:" + newsCount + "条!");
        SwingUtilities.invokeLater(() -> {
            String[] columnNames = {"标题", "发表时间"};
            Object[][] datas = new Object[Integer.valueOf(newsCount)][];
            try {
                String result = HttpClientPool.getHttpClient().get("https://roll.eastmoney.com/list?count=" + newsCount + "&type=fund&pageindex=1");
                List<NewsBean> newList = gson.fromJson(result, new TypeToken<List<NewsBean>>(){}.getType());

                for (int i = 0; i < newList.size(); i ++) {
                    NewsBean news = newList.get(i);
                    datas[i] = new Object[]{
                            "<html><u><a href='" + news.getUrl() + "'>" + news.getTitle() + "</a></u></html>",
                            news.getTime()
                    };
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.setDataVector(datas, columnNames);
            newsTable.setModel(model);
            resizeTable();
        });
    }

    private void resizeTable() {
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] > 0) {
                newsTable.getColumnModel().getColumn(i).setWidth(sizes[i]);
                newsTable.getColumnModel().getColumn(i).setPreferredWidth(sizes[i]);
            }
        }
    }

}
