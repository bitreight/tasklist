package com.bitreight.tasklist.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangeUserPasswordDto {

    private int id;

    @NotNull
    private String oldPassword;

    @NotNull
    @Size(min = 6, max = 20, message = "New password must be {min}..{max} characters long.")
    private String newPassword;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
