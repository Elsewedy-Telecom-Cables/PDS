package com.elsewedyt.pdsapp.models;

public class User {
    private int userId ;
    private int empCode ;
    private String userName ;
    private String password;
    private String fullName;
    private String phone ;
    private int role ;
    private int active;
    private String creationDate ;

    public User() {
        super();
    }

    public User(int id, int emp_id, String userName, String password, String fullName, String phone, int role, int active, String creationDate) {
        this.userId = id;
        this.empCode = emp_id;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.active = active;
        this.creationDate = creationDate;
    }

    public User(int emp_id, String userName, String fullName, String phone, int role, int active, String creationDate) {
        this.empCode = emp_id;
        this.userName = userName;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
        this.active = active;
        this.creationDate = creationDate;
    }

    public User(int id, String userName, String fullName, String phone, String creationDate) {
        this.userId = id;
        this.userName = userName;
        this.fullName = fullName;
        this.phone = phone;
        this.creationDate = creationDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmpCode() {
        return empCode;
    }

    public void setEmpCode(int empCode) {
        this.empCode = empCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return  fullName;
    }
}
