package model;

import java.util.List;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-05 21:22
 **/
public class DataSettings {

//    private String time;
//
//    private boolean hidenMoney;

    private List<TypeAlias> typeAliases;

//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public boolean getHidenMoney() {
//        return hidenMoney;
//    }
//
//    public void setHidenMoney(boolean hidenMoney) {
//        this.hidenMoney = hidenMoney;
//    }

    public List<TypeAlias> getTypeAliases() {
        return typeAliases;
    }

    public void setTypeAliases(List<TypeAlias> typeAliases) {
        this.typeAliases = typeAliases;
    }

}
