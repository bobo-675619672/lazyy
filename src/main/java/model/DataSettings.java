package model;

import java.util.List;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-05 21:22
 **/
public class DataSettings {
    private String template;
    private boolean hidenMoney;
    private List<TypeAlias> typeAliases;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<TypeAlias> getTypeAliases() {
        return typeAliases;
    }

    public void setTypeAliases(List<TypeAlias> typeAliases) {
        this.typeAliases = typeAliases;
    }

    public boolean getHidenMoney() {
        return hidenMoney;
    }

    public void setHidenMoney(boolean hidenMoney) {
        this.hidenMoney = hidenMoney;
    }

}
