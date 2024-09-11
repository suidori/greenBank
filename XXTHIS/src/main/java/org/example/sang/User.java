package org.example.sang;

import lombok.*;
@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {

//    user생성자...

    private String u_name;

    public User() {
        this.u_name = u_name;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "u_name='" + u_name + '\'' +
                '}';
    }
}
