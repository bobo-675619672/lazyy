package handler;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.ui.JBColor;
import constant.LazyyConstant;
import model.FundBean;
import model.TypeAlias;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import storage.LazyyHelperSettings;
import util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public abstract class FundRefreshHandler {

    private ArrayList<FundBean> data = new ArrayList<>();
    private JTable table;
    private JLabel label1;
    private JLabel label2;
    private int[] sizes = new int[]{0, 0, 0, 0, 0};

    private LazyyHelperSettings settings;

    public FundRefreshHandler(JTable table, JLabel label1, JLabel label2) {
        this.table = table;
        this.label1 = label1;
        this.label2 = label2;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Fix tree row height
        FontMetrics metrics = table.getFontMetrics(table.getFont());
        table.setRowHeight(Math.max(table.getRowHeight(), metrics.getHeight()));

        settings = ServiceManager.getService(LazyyHelperSettings.class);
    }

    public abstract void refresh(String flag, List<TypeAlias> code);

    public abstract void autoRefresh();

    /**
     * 更新全部数据
     */
    public void updateUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                recordTableSize();
                String[] columnNames = {"基金编码", "基金名称", "估算涨跌", "估算收益", "更新时间"};
                DefaultTableModel model = new DefaultTableModel(convertData(), columnNames);
                table.setModel(model);
                updateColors();
                resizeTable();

            }
        });
    }

    private void recordTableSize() {
        if (table.getColumnModel().getColumnCount() == 0) {
            return;
        }
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = table.getColumnModel().getColumn(i).getWidth();
        }
    }

    private void resizeTable() {
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] > 0) {
                table.getColumnModel().getColumn(i).setWidth(sizes[i]);
                table.getColumnModel().getColumn(i).setPreferredWidth(sizes[i]);
            }
        }
    }

    private void updateColors() {
        // 估计涨跌
        table.getColumn(table.getColumnName(2)).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0D;
                try {
                    // %
                    String s = value.toString().substring(0, value.toString().length() - 1);
                    temp = Double.parseDouble(s);
                } catch (Exception e) {

                }
                if (temp > 0) {
                    setForeground(JBColor.RED);
                } else if (temp < 0) {
                    setForeground(JBColor.GREEN);
                } else if (temp == 0) {
                    setForeground(JBColor.BLACK);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        // 估计收益
        table.getColumn(table.getColumnName(3)).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0D;
                try {
                    String s = value.toString();
                    // 无购买份数，无收益
                    if (!LazyyConstant.NONE_SHOW.equals(s)) {
                        temp = Double.parseDouble(s);
                    }
                } catch (Exception e) {

                }
                if (temp > 0D) {
                    setForeground(JBColor.RED);
                } else if (temp < 0D) {
                    setForeground(JBColor.GREEN);
                } else if (temp == 0D) {
                    setForeground(JBColor.BLACK);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }

    protected void updateData(FundBean bean) {
        int index = data.indexOf(bean);
        if (index >= 0) {
            data.set(index, bean);
        } else {
            data.add(bean);
        }
    }

    protected void updateDatas(List<FundBean> beans) {
        for (FundBean bean : beans) {
            updateData(bean);
        }
    }

    protected void updateShaLabel(String[] sha) {
        // 当天还没开始
//        if (getStartDate().after(new Date())) {
//            label2.setText(DateUtil.getCurDateFullStr() + "(赌场还没开门)");
//            return;
//        }
        // [上证指数,3317.9847,40.5445,1.24,1822997,25641104]
        String name = sha[0];
        String index1 = sha[1];
        String index2 = sha[2];
        String index3 = sha[3];

        String code2 = "↑";
        String code3 = "+";
        label1.setForeground(JBColor.RED);
        if (Double.valueOf(index3) < 0.0) {
            label1.setForeground(JBColor.GREEN);
            code2 = "↓";
            code3 = "-";
        }
        // 3317.9847 (↑40.5445 / +1.24%)
        String show = String.format("%s (%s%s / %s%s%%)", index1, code2, index2, code3, index3);
        label1.setText(show);
        // 当天已经结束
        if (canNowRefresh()) {
            label2.setText(DateUtil.getCurDateFullStr());
        } else {
            label2.setText(DateUtil.getCurDateStr() + " " + settings.getAdvancedSettings().getCloseTime() + "(赌场已经关门)");
        }
    }

    private Object[][] convertData() {
        // 隐藏收益
        boolean hidenMoney = settings.getGeneralSettings().isHidenMoney();
        boolean hidenTotalMoney = settings.getGeneralSettings().isHidenTotalMoney();

        int size = data.size();
        if (!hidenTotalMoney) {
            // 显示总数 大小+1
            size += 1;
        }
        Double totalMoney = 0D;
        Object[][] temp = new Object[size][];
        for (int i = 0; i < data.size(); i++) {
            FundBean fundBean = data.get(i);
            // 多线程问题
            if (null == fundBean) {
                break;
            }
            String timeStr = fundBean.getGztime();
            if (null == timeStr) {
                break;
            }
            String today = DateUtil.getCurDateStr();
            if (timeStr != null && timeStr.startsWith(today)) {
                timeStr = timeStr.substring(timeStr.indexOf(" "));
            }
            String gszzlStr = LazyyConstant.NONE_SHOW;
            if (fundBean.getGszzl() != null) {
                gszzlStr = fundBean.getGszzl().startsWith("-") ? fundBean.getGszzl() : "+" + fundBean.getGszzl();
            }
            String gslrStr = LazyyConstant.NONE_SHOW;
            if (!hidenMoney && StringUtils.isNotEmpty(fundBean.getNumber())) {
                double tempMoney = Double.valueOf(fundBean.getNumber()) * (Double.valueOf(fundBean.getGsz()) - Double.valueOf(fundBean.getDwjz()));
                totalMoney += tempMoney;
                gslrStr = String.format("%.2f", tempMoney);
            }
            temp[i] = new Object[]{fundBean.getFundCode(), fundBean.getFundName(), gszzlStr + "%", gslrStr, timeStr};
        }
        // 增加合计收益
        if (!hidenTotalMoney) {
            temp[size - 1] = new Object[]{LazyyConstant.NONE_SHOW, "合计:", "", String.format("%.2f", totalMoney), ""};
        }
        return temp;
    }


    public boolean canNowRefresh() {
        String curDate = DateUtil.getCurDateStr();
        String openTime = curDate + " " + settings.getAdvancedSettings().getOpenTime();
        String closeTime = curDate + " " + settings.getAdvancedSettings().getCloseTime();
        if (DateUtil.isNowBetween(openTime, closeTime)) {
            return true;
        }
        return false;
    }

    protected void clear() {
        data.clear();
    }

}
