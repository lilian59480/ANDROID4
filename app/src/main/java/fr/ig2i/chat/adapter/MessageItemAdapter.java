package fr.ig2i.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.ig2i.chat.R;
import fr.ig2i.chat.model.MessageWithUser;
import fr.ig2i.chat.viewholder.MessageItemViewHolder;

/**
 * Adapter for MessageItem
 * <p>
 * Adapters provide a binding from an app-specific data set to views that are displayed
 * within a {@link RecyclerView}.
 */
public class MessageItemAdapter extends RecyclerView.Adapter<MessageItemViewHolder> {

    private final List<MessageWithUser> items;

    private final MessageListener listener;
    private int currentUser;

    public MessageItemAdapter(List<MessageWithUser> item, int currentUser, MessageListener listener) {
        this.items = item;
        this.listener = listener;
        this.currentUser = currentUser;
    }

    /**
     * Called when RecyclerView needs a new {@link MessageItemViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(MessageItemViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(MessageItemViewHolder, int)
     */
    @NonNull
    @Override
    public MessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.fragment_message_item, parent, false);
        return new MessageItemViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link MessageItemViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link MessageItemViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MessageItemViewHolder holder, int position) {
        MessageWithUser itemData = this.items.get(position);
        holder.bind(itemData, this.currentUser);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.items.size();
    }


}
