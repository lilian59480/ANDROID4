package fr.ig2i.chat.viewholder;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import fr.ig2i.chat.R;
import fr.ig2i.chat.model.MessageWithUser;

/**
 * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
 */
public class MessageItemViewHolder extends RecyclerView.ViewHolder {

    public final View view;
    public final View cardView;
    public final TextView authorView;
    public final TextView messageView;

    public MessageItemViewHolder(@NonNull View view) {
        super(view);
        this.view = view;
        this.authorView = view.findViewById(R.id.message_item_author);
        this.cardView = view.findViewById(R.id.message_item_card);
        this.messageView = view.findViewById(R.id.message_item_content);
    }

    public void bind(@NonNull MessageWithUser itemData, int currentUser) {
        this.authorView.setText(itemData.getUser().getUsername());
        this.messageView.setText(itemData.getMessage());
        this.messageView.setTextColor(Color.parseColor(itemData.getUser().getColour()));
        if (itemData.getUser().isBlacklist()) {
            this.authorView.setPaintFlags(this.authorView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (itemData.getUser().isConnected()) {
            this.authorView.setTextColor(ResourcesCompat.getColor(this.view.getResources(), R.color.color_active, null));
        }
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " '" + this.messageView.getText() + "'";
    }
}
