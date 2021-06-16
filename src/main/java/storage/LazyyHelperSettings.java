package storage;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.rits.cloning.Cloner;
import constant.LazyyConstant;
import model.AdvancedSettings;
import model.DataSettings;
import model.GeneralSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

@State(name = "LazyyHelperSettings", storages = {@Storage("$APP_CONFIG$/LazyyHelperSettings-settings.xml")})
public class LazyyHelperSettings implements PersistentStateComponent<LazyyHelperSettings> {

    public LazyyHelperSettings() {

    }

    private DataSettings dataSettings;

    /**
     * 常规设置
     */
    private GeneralSettings generalSettings;

    /**
     * 高级设置
     */
    private AdvancedSettings advancedSettings;

    @Nullable
    @Override
    public LazyyHelperSettings getState() {
        if (this.dataSettings == null
                || this.advancedSettings == null
                || this.generalSettings == null) {
            loadDefaultDataSettings();
            loadDefaultGeneralSettings();
            loadDefaultAdvancedSettings();
        }
        return this;
    }

    @Override
    public void loadState(@NotNull LazyyHelperSettings lazzyHelperSettings) {
        XmlSerializerUtil.copyBean(lazzyHelperSettings, this);
    }

    public DataSettings getDateSettings() {
        if (dataSettings == null) {
            loadDefaultDataSettings();
        }
        return dataSettings;
    }

    public void setDateSettings(DataSettings dateSettings) {
        this.dataSettings = dateSettings;
    }

    public AdvancedSettings getAdvancedSettings() {
        if (advancedSettings == null) {
            loadDefaultAdvancedSettings();
        }
        return advancedSettings;
    }

    public void setAdvancedSettings(AdvancedSettings advancedSettings) {
        this.advancedSettings = advancedSettings;
    }

    public GeneralSettings getGeneralSettings() {
        if (null == generalSettings) {
            loadDefaultGeneralSettings();
        }
        return generalSettings;
    }

    public void setGeneralSettings(GeneralSettings generalSettings) {
        this.generalSettings = generalSettings;
    }

    @Override
    public LazyyHelperSettings clone() {
        Cloner cloner = new Cloner();
        cloner.nullInsteadOfClone();
        return cloner.deepClone(this);
    }

    private void loadDefaultDataSettings() {
        dataSettings = new DataSettings();
        dataSettings.setTypeAliases(new LinkedList<>());
    }

    private void loadDefaultAdvancedSettings() {
        advancedSettings = new AdvancedSettings();
        advancedSettings.setOpenTime(LazyyConstant.DEFAULT_OPEN_TIME);
        advancedSettings.setCloseTime(LazyyConstant.DEFAULT_CLOSE_TIME);
        advancedSettings.setNewsCount(LazyyConstant.DEFAULT_NEWS_COUNT);
        advancedSettings.setRefreshTime(LazyyConstant.DEFAULT_REFRESH_TIME);
    }

    private void loadDefaultGeneralSettings() {
        generalSettings = new GeneralSettings();
        generalSettings.setTime("10");
        generalSettings.setHiddenIncome(true);
        generalSettings.setHiddenHold(true);
        generalSettings.setHiddenFushi(true);
        // 目前不建议开启，没解决快捷键冲突问题
        generalSettings.setBossKey(false);
    }

}
