package fr.ig2i.chat.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * AsyncTask for proper and easy use of the UI thread.
 * This class perform background network operations and publish results through {@link RequestAsyncTaskCallback} without
 * having to manipulate threads and/or handlers.
 * <p>
 * An asynchronous task is defined by a computation that runs on a background thread and
 * whose result is published through {@link RequestAsyncTaskCallback}.
 * This asynchronous task need a generic definition of the result object.
 */
public class RequestAsyncTask<E> extends AsyncTask<Call<E>, Void, E> {

    private static final String TAG = RequestAsyncTask.class.getSimpleName();

    private final RequestAsyncTaskCallback<E> callback;
    private boolean failed = false;

    public RequestAsyncTask(@NonNull RequestAsyncTaskCallback<E> c) {
        super();
        this.callback = c;
    }

    /**
     * Perform networking operations on a background thread.
     * <p>
     * The specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     *
     * @param calls The parameters of the task.
     * @return A result, defined by the subclass of this task.
     */
    @Nullable
    @SafeVarargs
    @Override
    protected final E doInBackground(@NonNull final Call<E>... calls) {
        // Check for unsafe calls
        if (calls.length != 1) {
            throw new IllegalArgumentException("Only one argument is allowed");
        }

        final Call<E> call = calls[0];
        Response<E> response;

        // Do our request, and parse the response
        try {
            Log.i(RequestAsyncTask.TAG, "Executing the request " +
                    call.request().method() + " " +
                    call.request().url()
            );
            response = call.execute();
        } catch (IOException e) {
            Log.e(RequestAsyncTask.TAG, "Exception while running network task", e);
            this.failed = true;
            return null;
        }

        // Any non 200 response is a failure using REST
        this.failed = !response.isSuccessful();

        E result = response.body();

        Log.d(RequestAsyncTask.TAG, "Result : " + result);

        return result;

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
    protected void onPostExecute(@Nullable E result) {
        if (this.failed || result == null) {
            this.callback.onError(result);
        } else {
            this.callback.onSuccess(result);
        }
    }


}
