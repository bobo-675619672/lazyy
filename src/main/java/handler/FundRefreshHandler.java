package handler;

import com.intellij.ide.util.PropertiesComponent;
import constant.LazyyConstant;
import model.FundBean;
import com.intellij.ui.JBColor;
import model.TypeAlias;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public abstract class FundRefreshHandler {

    public static Set<String> init_set = new HashSet<>();

    private ArrayList<FundBean> data = new ArrayList<>();
    private JTable table;
    private JLabel label1;
    private JLabel label2;
    private int[] sizes = new int[]{0, 0, 0, 0, 0};
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FundRefreshHandler(JTable table, JLabel label1, JLabel label2) {
        this.table = table;
        this.label1 = label1;
        this.label2 = label2;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Fix tree row height
        FontMetrics metrics = table.getFontMetrics(table.getFont());
        table.setRowHeight(Math.max(table.getRowHeight(), metrics.getHeight()));
    }

    /**
     * 从网络更新数据
     * @param code
     */
    public abstract void handle(List<TypeAlias> code);

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
        if (table.getColumnModel().getColumnCount() == 0){
            return;
        }
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = table.getColumnModel().getColumn(i).getWidth();
        }
    }

    private void resizeTable() {
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] > 0){
                table.getColumnModel().getColumn(i).setWidth(sizes[i]);
                table.getColumnModel().getColumn(i).setPreferredWidth(sizes[i]);
            }
        }
    }

    private void updateColors() {
        table.getColumn(table.getColumnName(2)).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0.0;
                try {
                    // %
                    String s = value.toString().substring(0,value.toString().length()-1);
                    temp = Double.parseDouble(s);
                } catch (Exception e) {

                }
                Color orgin = getForeground();
                if (temp > 0) {
                    setForeground(JBColor.RED);
                } else if (temp < 0) {
                    setForeground(JBColor.GREEN);
                } else if (temp == 0) {
                    setForeground(orgin);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
        table.getColumn(table.getColumnName(3)).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                double temp = 0.0;
                try {
                    String s = value.toString();
                    temp = Double.parseDouble(s);
                } catch (Exception e) {

                }
                Color orgin = getForeground();
                if (temp > 0) {
                    setForeground(JBColor.RED);
                } else if (temp < 0) {
                    setForeground(JBColor.GREEN);
                } else if (temp == 0) {
                    setForeground(orgin);
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

    protected void updateShaLabel(String[] sha) {
        // 当天还没开始
        if (getStartDate().after(new Date())) {
            label2.setText(getCurDateStr() + "(赌场还没开门)");
            return;
        }
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
        if (getEndDate().before(new Date())) {
            label2.setText(getCurDayStr() + " " + LazyyConstant.TIME_ENT + "(赌场已经关门)");
        } else {
            label2.setText(getCurDateStr());
        }
    }

    private Object[][] convertData() {
        // 隐藏收益
        boolean hidenMoney = PropertiesComponent.getInstance().getBoolean(LazyyConstant.KEY_HIDENMINEY);

        Object[][] temp = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            FundBean fundBean = data.get(i);
            String timeStr = fundBean.getGztime();
            if(timeStr == null){
                break;
            }
            String today = getCurDayStr();
            if (timeStr != null && timeStr.startsWith(today)) {
                timeStr = timeStr.substring(timeStr.indexOf(" "));
            }
            String gszzlStr = "--";
            if (fundBean.getGszzl() != null) {
                gszzlStr= fundBean.getGszzl().startsWith("-") ? fundBean.getGszzl() : "+" + fundBean.getGszzl();
            }
            String gslrStr = "--";
            if (!hidenMoney && StringUtils.isNotEmpty(fundBean.getNumber())) {
                gslrStr = String.format("%.2f", Double.valueOf(fundBean.getNumber()) * (Double.valueOf(fundBean.getGsz()) - Double.valueOf(fundBean.getDwjz())));
            }
            temp[i] = new Object[]{fundBean.getFundCode(), fundBean.getFundName(), gszzlStr + "%", gslrStr, timeStr};
        }
        return temp;
    }

    public String getCurDateStr() {
        return dateFormat.format(new Date());
    }

    public String getCurDayStr() {
        return dayFormat.format(new Date());
    }

    public Date getStartDate() {
        try {
            return dateFormat.parse(getCurDayStr() + " " + LazyyConstant.TIME_START);
        } catch (Exception e) {

        }
        return null;
    }

    public Date getEndDate() {
        try {
            return dateFormat.parse(getCurDayStr() + " " + LazyyConstant.TIME_ENT);
        } catch (Exception e) {

        }
        return null;
    }

    public boolean canNowRefresh() {
        Date startDate = getStartDate();
        Date endDate = getEndDate();
        Date nowDate = new Date();
        // 开始 < 现在 < 结束
        if (startDate.before(nowDate) && nowDate.before(endDate)) {
            return true;
        }
        return false;
    }

    protected void clear(){
        data.clear();
    }

}
