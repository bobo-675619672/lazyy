package storage;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.rits.cloning.Cloner;
import constant.LazyyConstant;
import model.DataSettings;
import model.TypeAlias;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

@State(name = "LazyyHelperSettings", storages = {@Storage("$APP_CONFIG$/LazyyHelperSettings-settings.xml")})
public class LazyyHelperSettings implements PersistentStateComponent<LazyyHelperSettings> {

    public LazyyHelperSettings() {

    }

    private DataSettings dataSettings;

    @Nullable
    @Override
    public LazyyHelperSettings getState() {
        if (this.dataSettings == null) {
            loadDefaultSettings();
        }
        return this;
    }

    @Override
    public void loadState(@NotNull LazyyHelperSettings lazzyHelperSettings) {
        XmlSerializerUtil.copyBean(lazzyHelperSettings, this);
    }

    /**
     * Getter method for property <tt>codeTemplates</tt>.
     *
     * @return property value of codeTemplates
     */
    public DataSettings getDateSettings() {
        if (dataSettings == null) {
            loadDefaultSettings();
        }
        return dataSettings;
    }

    public void setDateSettings(DataSettings dateSettings) {
        this.dataSettings = dateSettings;
    }

    @Override
    public LazyyHelperSettings clone() {
        Cloner cloner = new Cloner();
        cloner.nullInsteadOfClone();
        return cloner.deepClone(this);
    }

    /**
     * 加载默认配置
     */
    private void loadDefaultSettings() {
        dataSettings = new DataSettings();
        try {
            List<TypeAlias> typeAliases = new LinkedList<>();
            dataSettings.setTypeAliases(typeAliases);
            PropertiesComponent.getInstance().setValue(LazyyConstant.KEY_TIME, "10");
            PropertiesComponent.getInstance().setValue(LazyyConstant.KEY_HIDENMINEY, true);
//            dataSettings.setHidenMoney(true);
//            dataSettings.setTime("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
