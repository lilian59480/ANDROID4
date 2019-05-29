package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

import fr.ig2i.chat.model.Conversation;

public class ConversationResult extends Result {
    private Conversation conversation;

    public ConversationResult() {
        this(null, null);
    }

    public ConversationResult(@Nullable Conversation conversation, @Nullable String status) {
        super(status);
        this.conversation = conversation;
    }

    @Nullable
    public Conversation getConversation() {
        return this.conversation;
    }

    public void setConversation(@Nullable Conversation conversation) {
        this.conversation = conversation;
    }
}
