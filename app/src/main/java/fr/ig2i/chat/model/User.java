package fr.ig2i.chat.model;

import androidx.annotation.Nullable;

public class User {

    private Integer id;
    private String username;
    private String password;
    private Boolean blacklist;
    private Boolean admin;
    private Boolean connected;
    private String colour;

    public User() {

    }

    @Nullable
    public Integer getId() {
        return this.id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    @Nullable
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getPassword() {
        return this.password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    @Nullable
    public Boolean isBlacklist() {
        return this.blacklist;
    }

    public void setBlacklist(@Nullable Boolean blacklist) {
        this.blacklist = blacklist;
    }

    @Nullable
    public Boolean isAdmin() {
        return this.admin;
    }

    public void setAdmin(@Nullable Boolean admin) {
        this.admin = admin;
    }

    @Nullable
    public Boolean isConnected() {
        return this.connected;
    }

    public void setConnected(@Nullable Boolean connected) {
        this.connected = connected;
    }

    @Nullable
    public String getColour() {
        return this.colour;
    }

    public void setColour(@Nullable String colour) {
        this.colour = colour;
    }
}
