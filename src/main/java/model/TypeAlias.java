package model;

public class TypeAlias extends DomainObject {
    public String code;
    public String number;
    public String hold;
    public String remark;

    public TypeAlias() {
    }

    public TypeAlias(String code, String number, String hold, String remark) {
        this.code = code;
        this.number = number;
        this.hold = hold;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHold() {
        return this.hold;
    }

    public void setHold(String hold) {
        this.hold = hold;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", this.code, this.number, this.hold, this.remark);
    }
}
