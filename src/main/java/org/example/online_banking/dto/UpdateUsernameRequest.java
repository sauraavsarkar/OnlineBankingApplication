package org.example.online_banking.dto;

public class UpdateUsernameRequest {
    private String newUsername;

    public UpdateUsernameRequest() {}

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}
