package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeAlias extends DomainObject {

    // 基金编码
    public String code;
    // 持有份数
    public String number;
    // 持仓价
    public String hold;
    // 备注
    public String remark;

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", this.code, this.number, this.hold, this.remark);
    }

}
