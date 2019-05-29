package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

public class LoginResult extends Result {
    private String token;
    private int id;

    public LoginResult() {
        this(null, null);
    }

    public LoginResult(@Nullable String token, @Nullable String status) {
        super(status);
        this.token = token;
    }

    @Nullable
    public String getToken() {
        return this.token;
    }

    public void setToken(@Nullable String token) {
        this.token = token;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
