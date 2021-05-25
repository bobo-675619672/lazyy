package model;

import lombok.Getter;
import lombok.Setter;

/**
 * 常规设置
 */
@Getter
@Setter
public class AdvancedSettings extends DomainObject {

    private String openTime;

    private String closeTime;

    private String newsCount;

}
