package entity;

import dto.response.UserInfo;

/**
 * Created by p on 2017/7/30.
 */
public class User {
    private String phone;
    private String name;
    private String gender;
    private String psw;
    private String token;

    public User() {
    }

    public User(String phone, String name, String psw, String token) {
        this.phone = phone;
        this.name = name;
        this.psw = psw;
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
