package com.bitreight.tasklist.dto;

import javax.validation.constraints.Size;

public class UserProfileDto {

    private int id;

    private String username;

    @Size(max = 20, message = "Name could be maximum {max} characters long.")
    private String name;

    @Size(max = 20, message = "Surname could be maximum {max} characters long.")
    private String surname;

    private String profileImagePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}
