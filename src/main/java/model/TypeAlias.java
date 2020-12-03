package model;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-06 21:11
 **/
public class TypeAlias extends DomainObject {
    public String code;
    public String number;
    public String money;
    public String remark;

    public TypeAlias() {
    }

    public TypeAlias(String code, String number, String money, String remark) {
        this.code = code;
        this.number = number;
        this.money = money;
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

    public String getMoney() {
        return this.money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", this.code, this.number, this.money, this.remark);
    }
}
