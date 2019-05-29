package fr.ig2i.chat.view.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import fr.ig2i.chat.R;
import fr.ig2i.chat.adapter.MessageItemAdapter;
import fr.ig2i.chat.adapter.MessageListener;
import fr.ig2i.chat.asynctask.MessageWithUserInfoAsyncTask;
import fr.ig2i.chat.asynctask.RequestAsyncTask;
import fr.ig2i.chat.asynctask.RequestAsyncTaskCallback;
import fr.ig2i.chat.model.Message;
import fr.ig2i.chat.model.MessageWithUser;
import fr.ig2i.chat.model.result.IdResult;
import retrofit2.Call;

/**
 * Show a conversation with messages.
 */
public class ShowChatActivity extends RepeatableActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public static final String ARG_CHAT_ID = "ARG_CHAT_ID";

    protected static final String TAG = ShowChatActivity.class.getSimpleName();

    protected SwipeRefreshLayout swipeView;
    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter rvAdapter;
    protected RecyclerView.LayoutManager rvLayoutManager;

    protected TextView messageField;
    protected Button validationField;

    protected List<MessageWithUser> messages;
    private AsyncTask currentTask;

    private int idconv = -1;

    private RequestAsyncTaskCallback<List<MessageWithUser>> callbackMessageListAsyncTask = new RequestAsyncTaskCallback<List<MessageWithUser>>() {

        private final ShowChatActivity sca = ShowChatActivity.this;

        @Override
        public void onSuccess(@NonNull List<MessageWithUser> result) {
            // Set new list
            this.sca.messages.clear();
            this.sca.messages.addAll(result);
            this.sca.rvAdapter.notifyDataSetChanged();
            this.sca.swipeView.setRefreshing(false);
        }

        @Override
        public void onError(@Nullable List<MessageWithUser> result) {
            this.sca.swipeView.setRefreshing(false);
            Log.e(ListChatActivity.TAG, "Non successful response");
            this.sca.errorLogout();
        }
    };

    private RequestAsyncTaskCallback<IdResult> createMessage = new RequestAsyncTaskCallback<IdResult>() {

        private final ShowChatActivity sca = ShowChatActivity.this;

        @Override
        public void onSuccess(@NonNull IdResult result) {
            this.sca.changeFields(true);

            int id = result.getId();

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

            this.sca.executeRequest();
            this.sca.messageField.setText("");
        }

        @Override
        public void onError(@Nullable IdResult result) {
            this.sca.changeFields(true);
            Log.e(ListChatActivity.TAG, "Non successful response");
            this.sca.errorLogout();
        }
    };


    /**
     * Perform initialisation.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show_conversation);

        this.idconv = this.getIntent().getIntExtra(ShowChatActivity.ARG_CHAT_ID, -1);

        this.swipeView = this.findViewById(R.id.list_message_swipe);
        this.swipeView.setOnRefreshListener(this);

        this.recyclerView = this.findViewById(R.id.list_message_recyclerview);

        this.messageField = this.findViewById(R.id.list_message_message_field);
        this.validationField = this.findViewById(R.id.list_message_validation_field);
        this.validationField.setOnClickListener(this);

        this.messages = new ArrayList<>();

        // use a linear layout manager
        this.rvLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.rvLayoutManager);

        // specify an adapter (see also next example)
        this.rvAdapter = new MessageItemAdapter(this.messages, this.globalState.getPreferencesManager().getUserId(), new MessageListener() {
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

    protected void executeRequest() {
        this.swipeView.setRefreshing(true);
        if (this.currentTask == null || this.currentTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.currentTask = new MessageWithUserInfoAsyncTask(this.callbackMessageListAsyncTask, this.globalState.getApiRepository(), this.globalState.getPreferencesManager().getToken()).execute(this.idconv);
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        this.executeRequest();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_message_validation_field:
                this.onFieldSubmit((Button) v);
                break;
        }
    }

    protected void changeFields(boolean enabled) {
        this.messageField.setEnabled(enabled);
        this.validationField.setEnabled(enabled);
    }

    @SuppressWarnings("unchecked")
    protected void onFieldSubmit(@NonNull Button b) {
        String message = this.messageField.getText().toString();

        this.changeFields(false);

        Message m = new Message();
        m.setIdConversation(this.idconv);
        m.setIdUser(this.globalState.getPreferencesManager().getUserId());
        m.setMessage(message);

        Call<IdResult> requestCall = this.globalState.getApiRepository().createMessage(this.globalState.getPreferencesManager().getToken(), this.idconv, m);

        // Start request
        new RequestAsyncTask<>(this.createMessage).execute(requestCall);
    }
}
