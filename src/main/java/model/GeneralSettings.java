package model;

import lombok.Getter;
import lombok.Setter;

/**
 * 常规设置
 */
@Getter
@Setter
public class GeneralSettings extends DomainObject {

    private boolean autoRefresh;

    private String time;

    private boolean hiddenIncome;

    private boolean hiddenFushi;

    private boolean hiddenHold;

    private boolean bossKey;

}
