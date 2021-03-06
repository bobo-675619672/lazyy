package model;

import com.google.gson.annotations.SerializedName;
import constant.LazyyConstant;

import java.util.Objects;

public class FundBean {
    @SerializedName("fundcode")
    private String fundCode;
    @SerializedName("name")
    private String fundName;
    private String jzrq;//净值日期
    private String dwjz;//当日净值
    private String gsz; //估算净值
    private String gszzl;//估算涨跌百分比 即-0.42%
    private String gztime;//gztime估值时间
    private String money;//购买金额
    private String number;//份数

    public FundBean() {
    }

    public FundBean(String fundCode) {
        this.fundCode = fundCode;
        this.fundName = LazyyConstant.NONE_SHOW;
    }

    public FundBean(TypeAlias alias) {
        this.fundCode = alias.getCode();
        this.money = alias.getMoney();
        this.number = alias.getNumber();
        this.fundName = "";
    }


    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getJzrq() {
        return jzrq;
    }

    public void setJzrq(String jzrq) {
        this.jzrq = jzrq;
    }

    public String getDwjz() {
        return dwjz;
    }

    public void setDwjz(String dwjz) {
        this.dwjz = dwjz;
    }

    public String getGsz() {
        return gsz;
    }

    public void setGsz(String gsz) {
        this.gsz = gsz;
    }

    public String getGszzl() {
        return gszzl;
    }

    public void setGszzl(String gszzl) {
        this.gszzl = gszzl;
    }

    public String getGztime() {
        return gztime;
    }

    public void setGztime(String gztime) {
        this.gztime = gztime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FundBean fundBean = (FundBean) o;
        return Objects.equals(fundCode, fundBean.fundCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fundCode);
    }
}
