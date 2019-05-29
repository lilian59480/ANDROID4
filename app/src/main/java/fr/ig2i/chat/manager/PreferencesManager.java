package fr.ig2i.chat.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import fr.ig2i.chat.R;

/**
 * Used to help interaction with {@link Preference} hierarchies from XML.
 */
public class PreferencesManager {

    private final SharedPreferences sharedPreferences;
    private final Context context;

    public PreferencesManager(@NonNull Context c) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        this.context = c;
    }

    @Nullable
    public String getUrl() {
        return this.getStringPreference(R.string.preference_networking_url_key);
    }

    @Nullable
    public String getToken() {
        return this.getStringPreference(R.string.preference_credentials_token_key);
    }

    public void setToken(@Nullable String token) {
        this.sharedPreferences.edit()
                .putString(this.context.getString(R.string.preference_credentials_token_key), token)
                .apply();
    }

    public boolean getRemember() {
        return this.getBooleanPreference(R.string.preference_remember_key);
    }

    @Nullable
    public String getUsername() {
        return this.getStringPreference(R.string.preference_credentials_username_key);
    }

    @Nullable
    public String getPassword() {
        return this.getStringPreference(R.string.preference_credentials_password_key);
    }

    public int getUserId() {
        return this.getIntegerPreference(R.string.preference_debug_userid_key);
    }

    public void setCredentials(int id, @Nullable String username, @Nullable String password, boolean remember) {
        this.sharedPreferences.edit()
                .putBoolean(this.context.getString(R.string.preference_remember_key), remember)
                .putInt(this.context.getString(R.string.preference_debug_userid_key), id)
                .apply();

        if (remember) {
            this.sharedPreferences.edit()
                    .putString(this.context.getString(R.string.preference_credentials_username_key), username)
                    .putString(this.context.getString(R.string.preference_credentials_password_key), password)
                    .apply();
        }
    }

    @Nullable
    private String getStringPreference(@StringRes int strRes) {
        return this.sharedPreferences.getString(this.context.getString(strRes), null);
    }

    private boolean getBooleanPreference(@StringRes int strRes) {
        return this.sharedPreferences.getBoolean(this.context.getString(strRes), false);
    }

    private int getIntegerPreference(@StringRes int strRes) {
        return this.sharedPreferences.getInt(this.context.getString(strRes), -1);
    }
}
