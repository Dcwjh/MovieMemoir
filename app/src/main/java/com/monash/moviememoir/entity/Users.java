package com.monash.moviememoir.entity;

import java.util.Date;


public class Users {

    private int userid;
    private String username;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String state;
    private String postcode;
    public Users(){

    }

    public Users(String username,String surname, String gender, String dob, String address, String state, String postcode){
        this.username = username;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.postcode = postcode;
    }

    public Users(String username, String surname){
        this.username = username;
        this.surname = surname;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", address='" + address + '\'' +
                ", state='" + state + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
