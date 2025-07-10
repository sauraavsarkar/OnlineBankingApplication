package org.example.online_banking.dto;

public class UpdatePasswordRequest {
    private String newPassword;

    public UpdatePasswordRequest() {}

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
