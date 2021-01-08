package setting.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ToolbarDecorator;
import model.AdvancedSettings;
import model.DataSettings;
import model.GeneralSettings;
import org.jetbrains.annotations.NotNull;
import storage.LazyyHelperSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SettingWindow {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel typeEditPenel;
    private JPanel templateEditPenel;
    private JCheckBox hidenCheckBox;
    private JComboBox timeComboBox;
    private JCheckBox hidenTotalCheckBox;
    private JTextField openTimeField;
    private JTextField closeTimeField;
    private JCheckBox hidenFushiCheckBox;
    private JCheckBox autoRefreshCheckBox;

    protected LazyyHelperSettings settings;
    private AliasTable aliasTable;

    public SettingWindow(LazyyHelperSettings settings) {
        //get setting
        this.settings = settings.clone();
        aliasTable = new AliasTable();
        //init   typeEditPenel
        typeEditPenel.add(
                ToolbarDecorator.createDecorator(aliasTable)
                        .setAddAction(button -> aliasTable.addAlias())
                        .setRemoveAction(button -> aliasTable.removeSelectedAliases())
                        .setEditAction(button -> aliasTable.editAlias())
                        .setMoveUpAction(anActionButton -> aliasTable.moveUp())
                        .setMoveDownAction(anActionButton -> aliasTable.moveDown())
                        .addExtraAction
                                (new AnActionButton("重置", AllIcons.Actions.Rollback) {
                                    @Override
                                    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                                        aliasTable.resetDefaultAliases();
                                    }
                                })
                        .createPanel(), BorderLayout.CENTER);
        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(MouseEvent e) {
                return aliasTable.editAlias();
            }
        }.installOn(aliasTable);
        // 常规设置
        boolean autoRefresh = this.settings.getGeneralSettings().isAutoRefresh();
        String time = this.settings.getGeneralSettings().getTime();
        boolean hidenMoney = this.settings.getGeneralSettings().isHidenMoney();
        boolean hidenTotalMoney = this.settings.getGeneralSettings().isHidenTotalMoney();
        boolean hidenFushi = this.settings.getGeneralSettings().isHidenFushi();
        autoRefreshCheckBox.setSelected(autoRefresh);
        this.refreshAction();
        timeComboBox.setSelectedItem(time);
        // 失效
        autoRefreshCheckBox.addActionListener(a -> this.refreshAction());

        hidenCheckBox.setSelected(hidenMoney);
        hidenTotalCheckBox.setSelected(hidenTotalMoney);
        hidenFushiCheckBox.setSelected(hidenFushi);
        // 高级设置
        String openTime = this.settings.getAdvancedSettings().getOpenTime();
        String closeTime = this.settings.getAdvancedSettings().getCloseTime();
        openTimeField.setText(openTime);
        closeTimeField.setText(closeTime);
    }

    public LazyyHelperSettings getSettings() {
        aliasTable.commit(settings);
        saveAdvancedSettingsModified();
        saveGeneralSettingsModified();
        return settings;
    }

    public void reset(LazyyHelperSettings settings) {
        this.settings = settings.clone();
        aliasTable.reset(settings.getDateSettings());
    }

    /**
     * 判断设置是否变更
     * @param settings
     * @return
     */
    public boolean isSettingsModified(LazyyHelperSettings settings) {
        if (isDataSettingsModified(settings.getDateSettings())
                || isGeneralSettingsModified(settings.getGeneralSettings())
                || isAdvancedSettingsModified(settings.getAdvancedSettings())) {
            return true;
        }
        return false;
    }

    public boolean isDataSettingsModified(DataSettings settings) {
        return aliasTable.isModified(settings);
    }

    public boolean isGeneralSettingsModified(GeneralSettings data) {
        if (!data.getTime().equals(timeComboBox.getSelectedItem().toString())
                || data.isAutoRefresh() != autoRefreshCheckBox.isSelected()
                || data.isHidenMoney() != hidenCheckBox.isSelected()
                || data.isHidenTotalMoney() != hidenTotalCheckBox.isSelected()
                || data.isHidenFushi() != hidenFushiCheckBox.isSelected()) {
            return true;
        }
        return false;
    }

    public boolean isAdvancedSettingsModified(AdvancedSettings data) {
        if (!data.getOpenTime().equals(openTimeField.getText())
                || !data.getCloseTime().equals(closeTimeField.getText())) {
            return true;
        }
        return false;
    }

    public AdvancedSettings saveAdvancedSettingsModified() {
        settings.getAdvancedSettings().setOpenTime(openTimeField.getText());
        settings.getAdvancedSettings().setCloseTime(closeTimeField.getText());
        return settings.getAdvancedSettings();
    }

    public GeneralSettings saveGeneralSettingsModified() {
        settings.getGeneralSettings().setAutoRefresh(autoRefreshCheckBox.isSelected());
        settings.getGeneralSettings().setTime(timeComboBox.getSelectedItem().toString());
        settings.getGeneralSettings().setHidenMoney(hidenCheckBox.isSelected());
        settings.getGeneralSettings().setHidenTotalMoney(hidenTotalCheckBox.isSelected());
        settings.getGeneralSettings().setHidenFushi(hidenFushiCheckBox.isSelected());
        return settings.getGeneralSettings();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void refreshAction() {
        timeComboBox.setEnabled(autoRefreshCheckBox.isSelected());
    }

}
