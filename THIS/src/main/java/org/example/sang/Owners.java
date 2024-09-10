package org.example.sang;

import lombok.Builder;

@Builder
public class Owners {
    private int u_idx;
    private int a_number;


    public Owners(int u_idx, int a_number) {
        this.u_idx = u_idx;
        this.a_number = a_number;
    }

    public int getU_idx() {
        return u_idx;
    }

    public void setU_idx(int u_idx) {
        this.u_idx = u_idx;
    }

    public int getA_number() {
        return a_number;
    }

    public void setA_number(int a_number) {
        this.a_number = a_number;
    }


    @Override
    public String toString() {
        return "계좌번호: " + a_number +  " / " +
                "예금주: " + u_idx
                ;

    }
}
