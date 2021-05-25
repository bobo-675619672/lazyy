package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TypeAlias alias = (TypeAlias) o;
        return Objects.equals(code, alias.code) &&
                Objects.equals(number, alias.number) &&
                Objects.equals(hold, alias.hold) &&
                Objects.equals(remark, alias.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code, number, hold, remark);
    }

}
