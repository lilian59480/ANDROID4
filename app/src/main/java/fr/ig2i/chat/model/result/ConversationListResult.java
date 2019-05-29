package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.ig2i.chat.model.Conversation;

public class ConversationListResult extends Result {
    private List<Conversation> conversations;

    public ConversationListResult() {
        this(new ArrayList<>(), null);
    }

    public ConversationListResult(@Nullable List<Conversation> conversations, @Nullable String status) {
        super(status);
        this.conversations = conversations;
    }

    @Nullable
    public List<Conversation> getConversations() {
        return this.conversations;
    }

    public boolean addConversation(@Nullable Conversation conversation) {
        return this.conversations.add(conversation);
    }
}
