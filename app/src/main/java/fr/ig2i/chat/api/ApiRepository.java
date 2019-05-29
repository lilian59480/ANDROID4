package fr.ig2i.chat.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import fr.ig2i.chat.manager.PreferencesManager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Classes that manage API calls.
 */
public class ApiRepository {

    private static final String TAG = ApiRepository.class.getSimpleName();

    private static final int CACHE_SIZE = 2 * 1024 * 1024; // 2 MB

    private ApiRepository() {

    }

    /**
     * Return a new {@link ApiService} with sensible default values.
     *
     * @param c Context to use.
     * @return A new {@link ApiService}.
     */
    @Nullable
    public static ApiService getApiService(@NonNull Context c) {
        PreferencesManager pm = new PreferencesManager(c);

        String url = pm.getUrl();
        if (url == null) {
            return null;
        }

        try {
            // Setup cache
            File appCacheDir = c.getCacheDir();
            Cache cache = new Cache(new File(appCacheDir, "http-cache/"), ApiRepository.CACHE_SIZE);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();

            // Setup Jackson, and ask not to fail with unknown properties
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Finally, create our Retrofit API
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                    .client(okHttpClient)
                    .baseUrl(url)
                    .build();
            return retrofit.create(ApiService.class);
        } catch (Exception ex) {
            Log.e(TAG, "Invalid Retrofit call", ex);
        }

        return null;
    }

}
