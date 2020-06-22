package com.monash.moviememoir.entity;

public class Credentials {
    private int registerid;
    private String username;
    private String password;
    private String singupdate;
    private Users userid;

    public Credentials(){

    }



    public Credentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    public int getRegisterid() {
        return registerid;
    }

    public void setRegisterid(int registerid) {
        this.registerid = registerid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSingupdate() {
        return singupdate;
    }

    public void setSingupdate(String singupdate) {
        this.singupdate = singupdate;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "registerid=" + registerid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", singupdate='" + singupdate + '\'' +
                ", userid=" + userid +
                '}';
    }
}
