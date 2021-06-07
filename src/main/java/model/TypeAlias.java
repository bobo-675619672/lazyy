package model;

import lombok.*;

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
    // 更新时间
    public String updated;

    @Override
    public String toString() {
        return "TypeAlias{" +
                "code='" + code + '\'' +
                ", number='" + number + '\'' +
                ", hold='" + hold + '\'' +
                ", remark='" + remark + '\'' +
                ", updated='" + updated + '\'' +
                '}';
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
                Objects.equals(remark, alias.remark) &&
                Objects.equals(updated, alias.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code, number, hold, remark, updated);
    }

}
