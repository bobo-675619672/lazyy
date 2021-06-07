package setting.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.table.JBTable;
import model.DataSettings;
import model.TypeAlias;
import org.jetbrains.annotations.NotNull;
import storage.LazyyHelperSettings;
import util.DateUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AliasTable extends JBTable {

    private static final Logger log = Logger.getInstance(AliasTable.class);
    private static final int COLUMN_CODE = 0;
    private static final int COLUMN_NUMBER = 1;
    private static final int COLUMN_HOLD = 2;
    private static final int COLUMN_REMARK = 3;
    private static final int COLUMN_UPDATED = 4;
    private final MyTableModel myTableModel = new MyTableModel();

    private List<TypeAlias> typeAliases = new LinkedList<>();

    /**
     * instantiation AliasTable
     */
    public AliasTable() {
        setModel(myTableModel);
        TableColumn codeColumn = getColumnModel().getColumn(COLUMN_CODE);
        TableColumn numberColumn = getColumnModel().getColumn(COLUMN_NUMBER);
        TableColumn moneyColumn = getColumnModel().getColumn(COLUMN_HOLD);
        TableColumn remarkColumn = getColumnModel().getColumn(COLUMN_REMARK);
        TableColumn updatedColumn = getColumnModel().getColumn(COLUMN_UPDATED);
//        column.setCellRenderer(new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//                final String macroValue = getAliasValueAt(row);
//                component.setForeground(macroValue.length() == 0
//                        ? JBColor.RED
//                        : isSelected ? table.getSelectionForeground() : table.getForeground());
//                return component;
//            }
//        });
        setColumnSize(codeColumn, 100, 150, 100);
        setColumnSize(numberColumn, 100, 150, 100);
        setColumnSize(moneyColumn, 100, 150, 100);
        setColumnSize(remarkColumn, 200, 250, 200);
        setColumnSize(updatedColumn, 150, 200, 150);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Set  Something  ColumnSize
     */
    public static void setColumnSize(TableColumn column, int preferedWidth, int maxWidth, int minWidth) {
        column.setPreferredWidth(preferedWidth);
        column.setMaxWidth(maxWidth);
        column.setMinWidth(minWidth);
    }

    public void addAlias() {
        final AliasEditor macroEditor = new AliasEditor("新增基金", "", "", "", "", DateUtil.getCurDateFullStr());
        if (macroEditor.showAndGet()) {
            final String name = macroEditor.getTitle();
            typeAliases.add(new TypeAlias(macroEditor.getCode(), macroEditor.getNumber(), macroEditor.getHold(), macroEditor.getRemark(), macroEditor.getUpdated()));
            final int index = indexOfAliasWithName(name);
            log.assertTrue(index >= 0);
            myTableModel.fireTableDataChanged();
            setRowSelectionInterval(index, index);
        }
    }

    private boolean isValidRow(int selectedRow) {
        return selectedRow >= 0 && selectedRow < typeAliases.size();
    }

    public void moveUp() {
        int selectedRow = getSelectedRow();
        int index1 = selectedRow - 1;
        if (selectedRow != -1) {
            Collections.swap(typeAliases, selectedRow, index1);
        }
        setRowSelectionInterval(index1, index1);
    }

    public void moveDown() {
        int selectedRow = getSelectedRow();
        int index1 = selectedRow + 1;
        if (selectedRow != -1) {
            Collections.swap(typeAliases, selectedRow, index1);
        }
        setRowSelectionInterval(index1, index1);
    }

    public void removeSelectedAliases() {
        final int[] selectedRows = getSelectedRows();
        if (selectedRows.length == 0) {
            return;
        }
        Arrays.sort(selectedRows);
        final int originalRow = selectedRows[0];
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            final int selectedRow = selectedRows[i];
            if (isValidRow(selectedRow)) {
                typeAliases.remove(selectedRow);
            }
        }
        myTableModel.fireTableDataChanged();
        if (originalRow < getRowCount()) {
            setRowSelectionInterval(originalRow, originalRow);
        } else if (getRowCount() > 0) {
            final int index = getRowCount() - 1;
            setRowSelectionInterval(index, index);
        }
    }

    public void commit(LazyyHelperSettings settings) {
        settings.getDateSettings().setTypeAliases(new LinkedList<>(typeAliases));
    }

    public void resetDefaultAliases() {
        myTableModel.fireTableDataChanged();
    }

    public void reset(DataSettings settings) {
        obtainAliases(typeAliases, settings);
        myTableModel.fireTableDataChanged();
    }

    private int indexOfAliasWithName(String name) {
        for (int i = 0; i < typeAliases.size(); i++) {
            final TypeAlias typeAlias = typeAliases.get(i);
            if (name.equals(typeAlias.getCode())) {
                return i;
            }
        }
        return -1;
    }

    private void obtainAliases(@NotNull List<TypeAlias> aliases, DataSettings settings) {
        aliases.clear();
        aliases.addAll(settings.getTypeAliases());
    }

    public boolean editAlias() {
        if (getSelectedRowCount() != 1) {
            return false;
        }
        final int selectedRow = getSelectedRow();
        final TypeAlias typeAlias = typeAliases.get(selectedRow);
        final AliasEditor editor = new AliasEditor("编辑基金", typeAlias.getCode(), typeAlias.getNumber(), typeAlias.getHold(), typeAlias.getRemark(), typeAlias.getUpdated());
        if (editor.showAndGet()) {
            typeAlias.setCode(editor.getCode());
            typeAlias.setNumber(editor.getNumber());
            typeAlias.setHold(editor.getHold());
            typeAlias.setRemark(editor.getRemark());
            typeAlias.setUpdated(editor.getUpdated());
            myTableModel.fireTableDataChanged();
        }
        return true;
    }

    public boolean isModified(DataSettings settings) {
        // 由于每次编辑都会保存数据，所以没办法判断是否改动
        return false;
    }

    //==========================================================================//

    /**
     * EditValidator
     */
    private static class EditValidator implements AliasEditor.Validator {
        @Override
        public boolean isOK(String name, String value) {
            return !name.isEmpty() && !value.isEmpty();
        }
    }


    /**
     * MyTableModel
     */
    private class MyTableModel extends AbstractTableModel {

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public int getRowCount() {
            return typeAliases.size();
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final TypeAlias pair = typeAliases.get(rowIndex);
            switch (columnIndex) {
                case COLUMN_CODE:
                    return pair.getCode();
                case COLUMN_NUMBER:
                    return pair.getNumber();
                case COLUMN_HOLD:
                    return pair.getHold();
                case COLUMN_REMARK:
                    return pair.getRemark();
                case COLUMN_UPDATED:
                    return pair.getUpdated();
            }
            log.error("Wrong indices");
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case COLUMN_CODE:
                    return "基金编码";
                case COLUMN_NUMBER:
                    return "持有份数";
                case COLUMN_HOLD:
                    return "持有价";
                case COLUMN_REMARK:
                    return "备注";
                case COLUMN_UPDATED:
                    return "更新时间";
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
