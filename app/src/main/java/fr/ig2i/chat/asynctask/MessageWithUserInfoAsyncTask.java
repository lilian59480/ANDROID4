package fr.ig2i.chat.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.ig2i.chat.api.ApiService;
import fr.ig2i.chat.model.Message;
import fr.ig2i.chat.model.MessageWithUser;
import fr.ig2i.chat.model.User;
import fr.ig2i.chat.model.result.MessageListResult;
import fr.ig2i.chat.model.result.UserResult;
import retrofit2.Call;
import retrofit2.Response;

/**
 * AsyncTask for proper and easy use of the UI thread.
 * This class perform background network operations and publish results through {@link RequestAsyncTaskCallback} without
 * having to manipulate threads and/or handlers.
 * <p>
 * An asynchronous task is defined by a computation that runs on a background thread and
 * whose result is published through {@link RequestAsyncTaskCallback}.
 * This asynchronous task has is generic already defined.
 */
public class MessageWithUserInfoAsyncTask extends AsyncTask<Integer, Void, List<MessageWithUser>> {

    private static final String TAG = MessageWithUserInfoAsyncTask.class.getSimpleName();
    private final RequestAsyncTaskCallback<List<MessageWithUser>> callback;
    private boolean failed = false;
    private String token;
    private SparseArray<User> cache = new SparseArray<>();

    private ApiService apiService;

    public MessageWithUserInfoAsyncTask(@NonNull RequestAsyncTaskCallback<List<MessageWithUser>> c, @NonNull ApiService apiService, @NonNull String token) {
        super();
        this.callback = c;
        this.apiService = apiService;
        this.token = token;
    }

    /**
     * Perform networking operations on a background thread.
     * <p>
     * The specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     *
     * @return A result.
     */
    @Nullable
    @Override
    protected final List<MessageWithUser> doInBackground(@NonNull final Integer... params) {
        // Check for unsafe calls
        if (params.length != 1) {
            throw new IllegalArgumentException("Only one argument is allowed");
        }

        int idconv = params[0];

        Call<MessageListResult> call = this.apiService.listMessages(this.token, idconv);

        Response<MessageListResult> response;

        // Do our request, and parse the response
        try {
            Log.i(MessageWithUserInfoAsyncTask.TAG, "Executing the request " +
                    call.request().method() + " " +
                    call.request().url()
            );
            response = call.execute();
        } catch (IOException e) {
            Log.e(MessageWithUserInfoAsyncTask.TAG, "Exception while running network task", e);
            this.failed = true;
            return null;
        }

        // Any non 200 response is a failure using REST
        this.failed = !response.isSuccessful();

        MessageListResult result = response.body();

        if (result == null) {
            Log.e(MessageWithUserInfoAsyncTask.TAG, "Null result");
            this.failed = true;
            return null;
        }

        List<MessageWithUser> listMessageWithUser = new ArrayList<>();

        // Iterate over all messages to get users to fetch
        for (Message m : result.getMessages()) {
            MessageWithUser msg = new MessageWithUser(m);

            // Using cache to improve speed
            User user = this.cache.get(m.getIdUser());
            if (user == null) {
                // Add to cache
                Call<UserResult> userCall = this.apiService.getUser(this.token, m.getIdUser());
                Response<UserResult> userResponse;
                // Do our request, and parse the response
                try {
                    Log.i(MessageWithUserInfoAsyncTask.TAG, "Executing the request " +
                            userCall.request().method() + " " +
                            userCall.request().url()
                    );
                    userResponse = userCall.execute();
                } catch (IOException e) {
                    Log.e(MessageWithUserInfoAsyncTask.TAG, "Exception while running network task", e);
                    this.failed = true;
                    return null;
                }

                // Any non 200 response is a failure using REST
                this.failed = !userResponse.isSuccessful();

                UserResult userResult = userResponse.body();

                if (userResult == null || userResult.getUser() == null) {
                    Log.e(MessageWithUserInfoAsyncTask.TAG, "Null result");
                    this.failed = true;
                    return null;
                }

                user = userResult.getUser();
                this.cache.append(m.getIdUser(), user);
            }

            msg.setUser(user);
            listMessageWithUser.add(msg);
        }

        return listMessageWithUser;

    }

    /**
     * Runs on the UI thread after {@link #doInBackground}.
     * The specified result is the value returned by {@link #doInBackground}.
     *
     * This method won't be invoked if the task was cancelled.
     *
     * @param result The result of the operation computed by {@link #doInBackground}.
     * @see #doInBackground
     */
    @Override
    protected void onPostExecute(@Nullable List<MessageWithUser> result) {
        if (this.failed || result == null) {
            this.callback.onError(result);
        } else {
            this.callback.onSuccess(result);
        }
    }
}
