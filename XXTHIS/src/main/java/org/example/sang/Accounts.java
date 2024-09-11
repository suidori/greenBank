package org.example.sang;

import lombok.Builder;

@Builder
public class Accounts {
    private int a_number;
    private int a_password;
    private int a_balance;

    public Accounts(int a_number, int a_password, int a_balance) {
        this.a_number = a_number;
        this.a_password = a_password;
        this.a_balance = a_balance;
    }

    public int getA_number() {
        return a_number;
    }

    public void setA_number(int a_number) {
        this.a_number = a_number;
    }

    public int getA_password() {
        return a_password;
    }

    public void setA_password(int a_password) {
        this.a_password = a_password;
    }

    public int getA_balance() {
        return a_balance;
    }

    public void setA_balance(int a_balance) {
        this.a_balance = a_balance;
    }


    @Override
    public String toString() {
        return "계좌번호= " + a_number +
                "잔액= " + a_balance
                ;
    }
}