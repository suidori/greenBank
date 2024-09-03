package org.example;

public class Accounts {
    private String aNumber;
    private double aBalance;


    public Accounts(String aNumber, double aBalance) {
        this.aNumber = aNumber;
        this.aBalance = aBalance;
    }

    public String getaNumber() {
        return aNumber;
    }

    public void setaNumber(String aNumber) {
        this.aNumber = aNumber;
    }

    public double getaBalance() {
        return aBalance;
    }

    public void setaBalance(double aBalance) {
        this.aBalance = aBalance;
    }


    @Override
    public String toString() {
        return "Account{" +
                "aNumber='" + aNumber + '\'' +
                ", aBalance=" + aBalance +
                '}';
    }
}