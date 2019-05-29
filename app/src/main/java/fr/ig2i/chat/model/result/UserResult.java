package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

import fr.ig2i.chat.model.User;

public class UserResult extends Result {
    private User user;

    public UserResult() {
        this(null, null);
    }

    public UserResult(@Nullable User user, @Nullable String status) {
        super(status);
        this.user = user;
    }

    @Nullable
    public User getUser() {
        return this.user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }
}
