public class History {

    private int h_calc;
    private int h_balance;
    private String h_timestamp;

    public History(int h_calc, int h_balance, String h_timestamp) {
        this.h_calc = h_calc;
        this.h_balance = h_balance;
        this.h_timestamp = h_timestamp;
    }

    @Override
    public String toString() {
        return "History{" +
                "h_calc=" + h_calc +
                ", h_balance=" + h_balance +
                ", h_timestamp='" + h_timestamp + '\'' +
                '}';
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
}
