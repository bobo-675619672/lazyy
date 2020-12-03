package setting.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.SearchableConfigurable;
import constant.LazyyConstant;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import storage.LazyyHelperSettings;

import javax.swing.*;

public class LazyyPanelConfigurable implements SearchableConfigurable {

    private SettingWindow settingWindow;

    private LazyyHelperSettings settings;

    public LazyyPanelConfigurable() {
        settings = ServiceManager.getService(LazyyHelperSettings.class);
    }

    @NotNull
    @Override
    public String getId() {
        return "plugins.lazyyHelper";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingWindow == null) {
            settingWindow = new SettingWindow(settings);
        }
        return settingWindow.getMainPanel();
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "help.lazyyHelper.configuration";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "lazyy";
    }

    @Override
    public void reset() {
        if (settingWindow != null) {
            settingWindow.reset(settings);
        }
    }

    @Override
    public boolean isModified() {
        return settingWindow != null && settingWindow.isSettingsModified(settings);
    }

    @Override
    public void apply() {
        settings.setDateSettings(settingWindow.getSettings().getDateSettings());
        settings = settingWindow.getSettings().clone();
        String time = settingWindow.getTime();
        boolean hidenMoney = settingWindow.isHidenMoney();
        boolean hidenTotalMoney = settingWindow.isHidenTotalMoney();
        PropertiesComponent.getInstance().setValue(LazyyConstant.KEY_TIME, time);
        PropertiesComponent.getInstance().setValue(LazyyConstant.KEY_HIDEN_MONEY, hidenMoney);
        PropertiesComponent.getInstance().setValue(LazyyConstant.KEY_HIDEN_TOTAL_MONEY, hidenTotalMoney);
    }

}
