package fr.ig2i.chat.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MessageWithUser extends Message {
    private User user;

    public MessageWithUser() {
        super();
    }

    public MessageWithUser(@NonNull Message m) {
        super();
        this.setId(m.getId());
        this.setIdConversation(m.getIdConversation());
        this.setIdUser(m.getIdUser());
        this.setMessage(m.getMessage());
    }

    @Nullable
    public User getUser() {
        return this.user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }
}
