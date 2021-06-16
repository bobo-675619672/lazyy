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
    private JCheckBox hiddenIncomeCheckBox;
    private JComboBox timeComboBox;
    private JTextField openTimeField;
    private JTextField closeTimeField;
    private JCheckBox hiddenFushiCheckBox;
    private JCheckBox autoRefreshCheckBox;
    private JCheckBox hiddenHoldCheckBox;
    private JTextField newsCountField;
    private JTextField refreshTimeField;
    private JCheckBox bossKeyCheckBox;

    protected LazyyHelperSettings settings;
    private AliasTable aliasTable;

    public SettingWindow(LazyyHelperSettings settings) {
        this.settings = settings.clone();
        aliasTable = new AliasTable();
        //初始化数据
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
        boolean hiddenMoney = this.settings.getGeneralSettings().isHiddenIncome();
        boolean hiddenFushi = this.settings.getGeneralSettings().isHiddenFushi();
        boolean hiddenHold = this.settings.getGeneralSettings().isHiddenHold();
        boolean bossKey = this.settings.getGeneralSettings().isBossKey();
        autoRefreshCheckBox.setSelected(autoRefresh);
        this.refreshAction();
        timeComboBox.setSelectedItem(time);
        // 失效
        autoRefreshCheckBox.addActionListener(a -> this.refreshAction());

        hiddenIncomeCheckBox.setSelected(hiddenMoney);
        hiddenFushiCheckBox.setSelected(hiddenFushi);
        hiddenHoldCheckBox.setSelected(hiddenHold);
        bossKeyCheckBox.setSelected(bossKey);
        // 高级设置
        String openTime = this.settings.getAdvancedSettings().getOpenTime();
        String closeTime = this.settings.getAdvancedSettings().getCloseTime();
        String newsCount = this.settings.getAdvancedSettings().getNewsCount();
        String refreshTime = this.settings.getAdvancedSettings().getRefreshTime();
        openTimeField.setText(openTime);
        closeTimeField.setText(closeTime);
        newsCountField.setText(newsCount);
        refreshTimeField.setText(refreshTime);
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
                || data.isHiddenIncome() != hiddenIncomeCheckBox.isSelected()
                || data.isHiddenFushi() != hiddenFushiCheckBox.isSelected()
                || data.isHiddenHold() != hiddenHoldCheckBox.isSelected()
                || data.isBossKey() != bossKeyCheckBox.isSelected()) {
            return true;
        }
        return false;
    }

    public boolean isAdvancedSettingsModified(AdvancedSettings data) {
        if (!data.getOpenTime().equals(openTimeField.getText())
                || !data.getCloseTime().equals(closeTimeField.getText())
                || !data.getNewsCount().equals(newsCountField.getText())
                || !data.getRefreshTime().equals(refreshTimeField.getText())) {
            return true;
        }
        return false;
    }

    public AdvancedSettings saveAdvancedSettingsModified() {
        settings.getAdvancedSettings().setOpenTime(openTimeField.getText());
        settings.getAdvancedSettings().setCloseTime(closeTimeField.getText());
        settings.getAdvancedSettings().setNewsCount(newsCountField.getText());
        settings.getAdvancedSettings().setRefreshTime(refreshTimeField.getText());
        return settings.getAdvancedSettings();
    }

    public GeneralSettings saveGeneralSettingsModified() {
        settings.getGeneralSettings().setAutoRefresh(autoRefreshCheckBox.isSelected());
        settings.getGeneralSettings().setTime(timeComboBox.getSelectedItem().toString());
        settings.getGeneralSettings().setHiddenIncome(hiddenIncomeCheckBox.isSelected());
        settings.getGeneralSettings().setHiddenFushi(hiddenFushiCheckBox.isSelected());
        settings.getGeneralSettings().setHiddenHold(hiddenHoldCheckBox.isSelected());
        settings.getGeneralSettings().setBossKey(bossKeyCheckBox.isSelected());
        return settings.getGeneralSettings();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void refreshAction() {
        timeComboBox.setEnabled(autoRefreshCheckBox.isSelected());
    }

}
