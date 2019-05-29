package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.ig2i.chat.model.Message;

public class MessageListResult extends Result {
    private List<Message> messages;

    public MessageListResult() {
        this(new ArrayList<>(), null);
    }

    public MessageListResult(@Nullable List<Message> messages, @Nullable String status) {
        super(status);
        this.messages = messages;
    }

    @Nullable
    public List<Message> getMessages() {
        return this.messages;
    }

    public boolean addUser(@Nullable Message user) {
        return this.messages.add(user);
    }
}
