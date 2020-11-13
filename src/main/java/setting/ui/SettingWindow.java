package setting.ui;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ToolbarDecorator;
import constant.LazyyConstant;
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

        String time = PropertiesComponent.getInstance().getValue(LazyyConstant.KEY_TIME);
        boolean hidenMoney = PropertiesComponent.getInstance().getBoolean(LazyyConstant.KEY_HIDENMINEY);

        hidenCheckBox.setSelected(hidenMoney);
        timeComboBox.setSelectedItem(time);
    }

    public LazyyHelperSettings getSettings() {
        aliasTable.commit(settings);
        return settings;
    }

    public void reset(LazyyHelperSettings settings) {
        this.settings = settings.clone();
        aliasTable.reset(settings);
    }


    public boolean isSettingsModified(LazyyHelperSettings settings) {
        if (aliasTable.isModified(settings)) {
            return true;
        }
        String time = PropertiesComponent.getInstance().getValue(LazyyConstant.KEY_TIME);
        if (!time.equals(getTime())) {
            return true;
        }
        boolean hidenMoney = PropertiesComponent.getInstance().getBoolean(LazyyConstant.KEY_HIDENMINEY);
        if (hidenMoney != isHidenMoney()) {
            return true;
        }
        return isModified(settings);
    }

    public boolean isModified(LazyyHelperSettings data) {
        if (settings.getDateSettings().getTypeAliases() == data.getDateSettings().getTypeAliases()) {
            return true;
        }
        return false;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getTime() {
        return timeComboBox.getSelectedItem().toString();
    }

    public boolean isHidenMoney() {
        return hidenCheckBox.isSelected();
    }

}
