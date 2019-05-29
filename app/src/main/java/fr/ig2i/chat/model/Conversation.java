package fr.ig2i.chat.model;

import androidx.annotation.Nullable;

public class Conversation {
    private Integer id;
    private Boolean active;
    private String theme;

    public Conversation() {

    }

    @Nullable
    public Integer getId() {
        return this.id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    @Nullable
    public Boolean isActive() {
        return this.active;
    }

    public void setActive(@Nullable Boolean active) {
        this.active = active;
    }

    @Nullable
    public String getTheme() {
        return this.theme;
    }

    public void setTheme(@Nullable String theme) {
        this.theme = theme;
    }
}
