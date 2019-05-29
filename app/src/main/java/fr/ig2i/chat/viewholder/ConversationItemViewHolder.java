package fr.ig2i.chat.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.ig2i.chat.R;
import fr.ig2i.chat.adapter.ConversationListeners;
import fr.ig2i.chat.model.Conversation;

/**
 * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
 */
public class ConversationItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    protected final TextView idView;
    protected final ImageView activeView;
    protected final TextView themeView;
    protected final ConversationListeners conversationListeners;
    protected int id = -1;
    protected Conversation item;

    public ConversationItemViewHolder(@NonNull View view, @NonNull ConversationListeners conversationListeners) {
        super(view);
        this.idView = view.findViewById(R.id.conversation_item_number);
        this.themeView = view.findViewById(R.id.conversation_item_content);
        this.activeView = view.findViewById(R.id.conversation_item_image);
        this.conversationListeners = conversationListeners;
    }

    public void bind(@NonNull Conversation itemData) {
        int id = itemData.getId();
        String idText = Integer.toString(itemData.getId());
        String theme = itemData.getTheme();
        boolean isActive = itemData.isActive();

        this.idView.setText(idText);
        this.themeView.setText(theme);
        if (isActive) {
            this.activeView.setImageResource(R.drawable.active_conversation);
        } else {
            this.activeView.setImageResource(R.drawable.inactive_conversation);
        }
        this.id = id;
        this.item = itemData;
        this.itemView.setOnClickListener(this);
        this.itemView.setOnLongClickListener(this);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " '" + this.themeView.getText() + "'";
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(@NonNull View v) {
        if (this.id != -1) {
            this.conversationListeners.onClick(this.id);
        }
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(@NonNull View v) {
        if (this.id != -1) {
            this.conversationListeners.onLongClick(this.id, this.item);
        }
        return true;
    }
}
