package setting.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import storage.LazyyHelperSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

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

        timeComboBox.setSelectedItem(settings.getDateSettings().getTemplate());
        hidenCheckBox.setEnabled(settings.getDateSettings().getHidenMoney());
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
        if (aliasTable.isModified(settings)) return true;
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

}
