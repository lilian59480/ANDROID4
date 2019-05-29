package fr.ig2i.chat.model.parameter;

import androidx.annotation.NonNull;

public class LoginParameters {
    private String username;
    private String password;

    public LoginParameters(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    @NonNull
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getPassword() {
        return this.password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }
}
