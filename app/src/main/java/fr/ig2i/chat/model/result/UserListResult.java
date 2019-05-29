package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.ig2i.chat.model.User;

public class UserListResult extends Result {
    private List<User> users;

    public UserListResult() {
        this(new ArrayList<>(), null);
    }

    public UserListResult(@Nullable List<User> users, @Nullable String status) {
        super(status);
        this.users = users;
    }

    @Nullable
    public List<User> getUsers() {
        return this.users;
    }

    public boolean addUser(@Nullable User user) {
        return this.users.add(user);
    }
}
