package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

import fr.ig2i.chat.model.Message;

public class MessageResult extends Result {
    private Message message;

    public MessageResult() {
        this(null, null);
    }

    public MessageResult(@Nullable Message message, @Nullable String status) {
        super(status);
        this.message = message;
    }

    @Nullable
    public Message getMessage() {
        return this.message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }
}
