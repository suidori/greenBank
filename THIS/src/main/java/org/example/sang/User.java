package org.example.sang;

import lombok.*;

@Builder
public class User {

//    user생성자...

    private String u_name;
    private int u_idx;
    private String u_id;

    public User(String u_name, int u_idx, String u_id) {
        this.u_name = u_name;
        this.u_idx = u_idx;
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
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

    @Override
    public String toString() {
        return "User{" +
                "u_name='" + u_name + '\'' +
                '}';
    }
}
