package fr.ig2i.chat.adapter;

import androidx.annotation.NonNull;

import fr.ig2i.chat.model.Conversation;

/**
 * Classes that wish to be notified when there is an interaction.
 */
public interface ConversationListeners {
    /**
     * Called after a click.
     */
    void onClick(int idconv);

    /**
     * Called after a long click.
     */
    void onLongClick(int idconv, @NonNull Conversation conversation);
}
