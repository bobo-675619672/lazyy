package model;

public class GeneralSettings extends DomainObject {

    private boolean autoRefresh;

    private String time;

    private boolean hidenMoney;

    private boolean hidenTotalMoney;

    private boolean hidenFushi;

    private boolean hidenHold;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isHidenMoney() {
        return hidenMoney;
    }

    public void setHidenMoney(boolean hidenMoney) {
        this.hidenMoney = hidenMoney;
    }

    public boolean isHidenTotalMoney() {
        return hidenTotalMoney;
    }

    public void setHidenTotalMoney(boolean hidenTotalMoney) {
        this.hidenTotalMoney = hidenTotalMoney;
    }

    public boolean isHidenFushi() {
        return hidenFushi;
    }

    public void setHidenFushi(boolean hidenFushi) {
        this.hidenFushi = hidenFushi;
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public boolean isHidenHold() {
        return hidenHold;
    }

    public void setHidenHold(boolean hidenHold) {
        this.hidenHold = hidenHold;
    }
}
