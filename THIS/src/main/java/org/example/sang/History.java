package org.example.sang;


import lombok.Builder;
import java.sql.Timestamp;


@Builder
public class History {

    private int a_number;
    private int u_idx;
    private int h_calc;
    private int h_balance;
    private String h_timestamp;

    public History(int a_number, int u_idx, int h_cal, int h_balance, String h_timestamp) {
        this.a_number = a_number;
        this.u_idx = u_idx;
        this.h_calc = h_cal;
        this.h_balance = h_balance;
        this.h_timestamp = h_timestamp;
    }

    public int getA_number() {
        return a_number;
    }

    public void setA_number(int a_number) {
        this.a_number = a_number;
    }

    public int getU_idx() {
        return u_idx;
    }

    public void setU_idx(int u_idx) {
        this.u_idx = u_idx;
    }

    public int getH_calc() {
        return h_calc;
    }

    public void setH_calc(int h_calc) {
        this.h_calc = h_calc;
    }

    public int getH_balance() {
        return h_balance;
    }

    public void setH_balance(int h_balance) {
        this.h_balance = h_balance;
    }

    public String getH_timestamp() {
        return h_timestamp;
    }

    public void setH_timestamp(String h_timestamp) {
        this.h_timestamp = h_timestamp;
    }


    @Override
    public String toString() {
        return "History{" +
                "a_number=" + a_number +
                ", u_idx=" + u_idx +
                ", h_cal=" + h_calc +
                ", h_balance=" + h_balance +
                ", h_timestamp='" + h_timestamp + '\'' +
                '}';
    }
}
