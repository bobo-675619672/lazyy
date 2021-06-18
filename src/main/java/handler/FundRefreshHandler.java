package handler;

import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.ui.JBColor;
import constant.LazyyConstant;
import model.FundBean;
import org.apache.commons.lang3.StringUtils;
import storage.LazyyHelperSettings;
import util.DateUtil;
import util.StringUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class FundRefreshHandler {

    private ArrayList<FundBean> data = new ArrayList<>();
    private JTable table;
    private JLabel aLabel;
    private JLabel bLabel;
    private JComboBox codeComboBox;

    private int[] sizes = new int[]{80, 180, 80, 80, 80, 80, 80};
    private String[] columnNames = {"基金编码", "基金名称", "估算涨跌", "估算收益", "持有收益", "持有收益率", "更新时间"};

    private LazyyHelperSettings settings;

    public FundRefreshHandler(JTable table, JComboBox codeComboBox, JLabel aLabel, JLabel bLabel) {
        this.table = table;
        this.aLabel = aLabel;
        this.bLabel = bLabel;
        this.codeComboBox = codeComboBox;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Fix tree row height
        FontMetrics metrics = table.getFontMetrics(table.getFont());
        table.setRowHeight(Math.max(table.getRowHeight(), metrics.getHeight()));

        settings = ServiceManager.getService(LazyyHelperSettings.class);
    }

    public abstract void refresh();

    public abstract void autoRefresh();

    /**
     * 更新全部数据
     */
    public void updateUI() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 不可编辑
                return false;
            }
        };
        model.setDataVector(this.convertData(), getColumn(columnNames));
        table.setModel(model);
        updateColors();
        resizeTable();
    }

    private void resizeTable() {
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            if (sizes[i] > 0) {
                table.getColumnModel().getColumn(i).setWidth(sizes[i]);
                table.getColumnModel().getColumn(i).setPreferredWidth(sizes[i]);
            }
        }
    }

    private void updateColors() {
        /*
        2:估计涨跌
        3:估计收益
        4:持有收益
        5:持有收益率
         */
        List<TableColumn> list = Lists.newArrayList();
        list.add(table.getColumn(table.getColumnName(2)));
        if (!settings.getGeneralSettings().isHiddenIncome()) {
            list.add(table.getColumn(table.getColumnName(3)));
        }
        if (!settings.getGeneralSettings().isHiddenHold()) {
            list.add(table.getColumn(table.getColumnName(4)));
            list.add(table.getColumn(table.getColumnName(5)));
        }
        updateColors(list);
    }

    private void updateColors(List<TableColumn> columns) {
        for (TableColumn column : columns) {
            column.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    double temp = 0D;
                    try {
                        // 去掉百分号
                        String s = value.toString().replaceAll("%", "");
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
    }

    protected void updateData(List<FundBean> beans) {
        data.clear();
        data.addAll(beans);
    }

    protected void updateShaLabel(String[] sha) {
        // 当天还没开始
//        if (getStartDate().after(new Date())) {
//            label2.setText(DateUtil.getCurDateFullStr() + "(赌场还没开门)");
//            return;
//        }
        // [上证指数,3317.9847,40.5445,1.24,1822997,25641104]
        String name = sha[0];
        String index1 = StringUtil.getTwo(sha[1]);
        String index2 = StringUtil.getTwo(sha[2]);
        String index3 = sha[3];

        String code2 = "↑";
        String code3 = "+";
        aLabel.setForeground(JBColor.RED);
        if (Double.valueOf(index3) < 0.0) {
            aLabel.setForeground(JBColor.GREEN);
            code2 = "↓";
            code3 = "-";
        }
        // 3317.98 (↑40.55 / +1.24%)
        String show = String.format("%s (%s%s / %s%s%%)", index1, code2, index2, code3, index3);
        aLabel.setText(show);
        // 当天已经结束
        if (canNowRefresh()) {
            bLabel.setText(DateUtil.getCurDateFullStr());
        } else {
            bLabel.setText(DateUtil.getCurDateStr() + " " + settings.getAdvancedSettings().getCloseTime() + "(赌场关门)");
        }
    }

    private Object[][] convertData() {
        // 隐藏收益
        boolean hiddenIncome = settings.getGeneralSettings().isHiddenIncome();
        // 持有收益/率
        boolean hiddenHold = settings.getGeneralSettings().isHiddenHold();
        int size = data.size();
        if (!hiddenIncome) {
            // 显示总数 大小+1
            size += 1;
        }
        Double totalIncome = 0D;
        Double totalHold = 0D;
        Object[][] temp = new Object[size][];
        for (int i = 0; i < data.size(); i++) {
            FundBean fundBean = data.get(i);
            // 多线程问题
            if (null == fundBean) {
                break;
            }
            String timeStr = fundBean.getGztime();
            Double gsz = Double.valueOf(fundBean.getGsz());
            Double dwjz = Double.valueOf(fundBean.getDwjz());
            if (null == timeStr) {
                break;
            }
            String today = DateUtil.getCurDateStr();
            if (timeStr != null && timeStr.startsWith(today)) {
                timeStr = timeStr.substring(timeStr.indexOf(" "));
            }
            // 估算涨跌
            String gszzlStr = LazyyConstant.NONE_SHOW;
            if (fundBean.getGszzl() != null) {
                gszzlStr = fundBean.getGszzl().startsWith("-") ? fundBean.getGszzl() : "+" + fundBean.getGszzl();
            }
            // 估算收益
            String gslrStr = LazyyConstant.NONE_SHOW;
            if (!hiddenIncome && StringUtils.isNotEmpty(fundBean.getNumber())) {
                Double number = Double.valueOf(fundBean.getNumber());
                // 估算收益 = 持有份数 * (估算净值 - 当日净值)
                double tempMoney = number * (gsz - dwjz);
                totalIncome += tempMoney;
                gslrStr = String.format("%.2f", tempMoney);
            }
            // 持有收益、持有收益率
            String holdStr = LazyyConstant.NONE_SHOW;
            String holdRateStr = LazyyConstant.NONE_SHOW;
            if (!hiddenHold && StringUtils.isNotEmpty(fundBean.getNumber()) && StringUtils.isNotEmpty(fundBean.getHold())) {
                Double number = Double.valueOf(fundBean.getNumber());
                Double hold = Double.valueOf(fundBean.getHold());
                // 持有收益 = 持有份数 * (当日净值 - 持有成本价)
                double holdMoney = number * (dwjz - hold);
                // 持有收益率 = (当日净值 - 持有成本价) / 持有成本价
                double holdRate = 100.0 * (dwjz - hold) / hold;
                totalHold += holdMoney;
                // 保留2位小数
                holdStr = String.format("%.2f", holdMoney);
                holdRateStr = String.format("%.2f", holdRate) + "%";
            }
            temp[i] = getColumn(new Object[]{
                    fundBean.getFundCode(),
                    fundBean.getFundName(),
                    gszzlStr + "%",
                    gslrStr,
                    holdStr,
                    holdRateStr,
                    timeStr
            });
        }
        // 增加合计收益
        if (!hiddenIncome) {
            String totalHoldStr = LazyyConstant.NONE_SHOW;
            if (0D != totalHold) {
                totalHoldStr = String.format("%.2f", totalHold);
            }
            temp[size - 1] = getColumn(new Object[]{
                    LazyyConstant.NONE_SHOW,
                    "合计:",
                    LazyyConstant.NONE_SHOW,
                    String.format("%.2f", totalIncome),
                    totalHoldStr,
                    LazyyConstant.NONE_SHOW,
                    LazyyConstant.NONE_SHOW
            });
        }
        return temp;
    }

    private Object[] getColumn(Object[] objects) {
        // 隐藏收益
        boolean hiddenIncome = settings.getGeneralSettings().isHiddenIncome();
        // 持有收益/率
        boolean hiddenHold = settings.getGeneralSettings().isHiddenHold();
        List<Object> nameList = Lists.newArrayListWithCapacity(objects.length);
        nameList.add(objects[0]);
        nameList.add(objects[1]);
        nameList.add(objects[2]);
        if (!hiddenIncome) {
            nameList.add(objects[3]);
        }
        if (!hiddenHold) {
            nameList.add(objects[4]);
            nameList.add(objects[5]);
        }
        nameList.add(objects[6]);
        return nameList.toArray();
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
