package fr.ig2i.chat.asynctask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Classes that is used to operate on the UI with an already resolved request.
 */
public interface RequestAsyncTaskCallback<E> {
    /**
     * Called after a success.
     *
     * @param result The result.
     */
    void onSuccess(@NonNull E result);

    /**
     * Called after an error.
     *
     * @param result The result or null if there is a network error.
     */
    void onError(@Nullable E result);
}
