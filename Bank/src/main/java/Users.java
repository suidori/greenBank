public class Users {

    private int u_idx;
    private String u_id;
    private String u_password;
    private String u_level;
    private String u_name;
    private String u_phone;

    public Users(int u_idx, String u_id, String u_password, String u_level, String u_name, String u_phone) {
        this.u_idx = u_idx;
        this.u_id = u_id;
        this.u_password = u_password;
        this.u_level = u_level;
        this.u_name = u_name;
        this.u_phone = u_phone;
    }

    public int getU_idx() {
        return u_idx;
    }

    public void setU_idx(int u_idx) {
        this.u_idx = u_idx;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public String getU_level() {
        return u_level;
    }

    public void setU_level(String u_level) {
        this.u_level = u_level;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }
}
