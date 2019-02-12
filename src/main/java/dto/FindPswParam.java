package dto;

/**
 * Created by p on 2017/7/31.
 */
public class FindPswParam {
    private String phone;
    private String psw;
    private String vercode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getVerCode() {
        return vercode;
    }

    public void setVercode(String verCode) {
        this.vercode = verCode;
    }

}
