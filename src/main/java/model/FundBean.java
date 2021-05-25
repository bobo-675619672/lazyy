package model;

import com.google.gson.annotations.SerializedName;
import constant.LazyyConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundBean {

    @SerializedName("fundcode")
    private String fundCode;
    @SerializedName("name")
    private String fundName;
    // 净值日期
    private String jzrq;
    // 当日净值
    private String dwjz;
    // 估算净值
    private String gsz;
    // 估算涨跌百分比 即-0.42%
    private String gszzl;
    // gztime估值时间
    private String gztime;
    // 持有价
    private String hold;
    // 份数
    private String number;

    public FundBean(String fundCode) {
        this.fundCode = fundCode;
        this.fundName = LazyyConstant.NONE_SHOW;
    }

    public FundBean(TypeAlias alias) {
        this.fundCode = alias.getCode();
        this.hold = alias.getHold();
        this.number = alias.getNumber();
        this.fundName = "";
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
