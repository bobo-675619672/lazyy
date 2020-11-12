package storage;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.rits.cloning.Cloner;
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

    public void updateTemplate(String template) {
        dataSettings.setTemplate(template);
    }

    public void updateTypeMap(List<TypeAlias> typeAliases) {
        dataSettings.setTypeAliases(typeAliases);
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
            dataSettings.setTemplate("--");
            dataSettings.setHidenMoney(true);
            List<TypeAlias> typeAliases = new LinkedList<>();
            dataSettings.setTypeAliases(typeAliases);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
