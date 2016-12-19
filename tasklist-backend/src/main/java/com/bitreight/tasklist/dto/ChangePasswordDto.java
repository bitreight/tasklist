package com.bitreight.tasklist.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ChangePasswordDto {

    private int userId;

    @NotNull
    private String oldPassword;

    @NotNull
    @Size(min = 6, max = 20, message = "New password must be {min}..{max} characters long.")
    private String newPassword;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
