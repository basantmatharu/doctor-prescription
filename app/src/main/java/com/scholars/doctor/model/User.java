package com.scholars.doctor.model;

import java.io.Serializable;

/**
 * Created by I311846 on 06-Jun-16.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 9091565695596698402L;

    private String username;

    private String fullName;

    private String passwordHash;

    private String registrationId;

    private String role;

    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {

        return address;
    }

    public User() {}

    public User(String username, String fullName, String passwordHash, String address, String role, String registrationId) {
        this.username = username;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.role = role;
        this.address = address;
        this.registrationId = registrationId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
