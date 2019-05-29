package fr.ig2i.chat.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import fr.ig2i.chat.R;
import fr.ig2i.chat.adapter.ConversationItemAdapter;
import fr.ig2i.chat.adapter.ConversationListeners;
import fr.ig2i.chat.asynctask.RequestAsyncTask;
import fr.ig2i.chat.asynctask.RequestAsyncTaskCallback;
import fr.ig2i.chat.model.Conversation;
import fr.ig2i.chat.model.result.ConversationListResult;
import fr.ig2i.chat.model.result.Result;

/**
 * List all conversations.
 */
public class ListChatActivity extends RepeatableActivity implements SwipeRefreshLayout.OnRefreshListener {

    protected static final String TAG = ListChatActivity.class.getSimpleName();

    protected SwipeRefreshLayout swipeView;
    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter rvAdapter;
    protected RecyclerView.LayoutManager rvLayoutManager;
    protected List<Conversation> conversations;

    private RequestAsyncTaskCallback<ConversationListResult> callbackConversationListAsyncTask = new RequestAsyncTaskCallback<ConversationListResult>() {

        private final ListChatActivity lca = ListChatActivity.this;

        @Override
        public void onSuccess(@NonNull ConversationListResult result) {
            List<Conversation> listConversation = result.getConversations();

            if (listConversation == null) {
                result.setStatus("Invalid list");
                this.onError(result);
                return;
            }

            // Set new list
            this.lca.conversations.clear();
            this.lca.conversations.addAll(listConversation);
            this.lca.rvAdapter.notifyDataSetChanged();
            this.lca.swipeView.setRefreshing(false);
        }

        @Override
        public void onError(@Nullable ConversationListResult result) {
            this.lca.swipeView.setRefreshing(false);
            Log.e(ListChatActivity.TAG, "Non successful response");
            this.lca.errorLogout();
        }
    };
    private AsyncTask currentTask;
    private RequestAsyncTaskCallback<Result> updateConversation = new RequestAsyncTaskCallback<Result>() {

        private final ListChatActivity lca = ListChatActivity.this;

        @Override
        public void onSuccess(@NonNull Result result) {
            String status = result.getStatus();

            if (TextUtils.isEmpty(status)) {
                result.setStatus("Invalid status");
                this.onError(result);
                return;
            }

            if (!TextUtils.equals("ok", status)) {
                result.setStatus("Error");
                this.onError(result);
                return;
            }

            // Update entries
            this.lca.executeRequest();
        }

        @Override
        public void onError(@Nullable Result result) {
            this.lca.swipeView.setRefreshing(false);
            Log.e(ListChatActivity.TAG, "Non successful response");
            this.lca.errorLogout();
        }
    };

    /**
     * Perform initialisation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_list_conversation);

        this.swipeView = this.findViewById(R.id.choice_conversation_swipe);
        this.swipeView.setOnRefreshListener(this);

        this.recyclerView = this.findViewById(R.id.choice_conversation_recyclerview);

        this.conversations = new ArrayList<>();

        // use a linear layout manager
        this.rvLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.rvLayoutManager);

        // specify an adapter (see also next example)
        this.rvAdapter = new ConversationItemAdapter(this.conversations, new ConversationListeners() {
            @Override
            public void onClick(int idconv) {
                Intent i = new Intent(ListChatActivity.this, ShowChatActivity.class);
                i.putExtra(ShowChatActivity.ARG_CHAT_ID, idconv);
                ListChatActivity.this.startActivity(i);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onLongClick(int idconv, @NonNull Conversation conversation) {
                conversation.setActive(!conversation.isActive());
                new RequestAsyncTask<>(ListChatActivity.this.updateConversation).execute(
                        ListChatActivity.this.globalState.getApiRepository().updateConversation(
                                ListChatActivity.this.globalState.getPreferencesManager().getToken(),
                                conversation.getId(),
                                conversation
                        )
                );
            }
        });
        this.recyclerView.setAdapter(this.rvAdapter);
    }

    /**
     * Called after {@link #onCreate} - or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     */
    @Override
    protected void onStart() {
        super.onStart();
        this.executeRequest();
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.executeRequest();
    }

    @SuppressWarnings("unchecked")
    protected void executeRequest() {
        this.swipeView.setRefreshing(true);
        if (this.currentTask == null || this.currentTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.currentTask = new RequestAsyncTask<>(this.callbackConversationListAsyncTask).execute(
                    this.globalState.getApiRepository().listConversations(this.globalState.getPreferencesManager().getToken())
            );
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        this.executeRequest();
    }
}
