package fr.ig2i.chat.manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

/**
 * A {@link Preference} that allows for int input.
 * <p>
 * It is a subclass of {@link EditTextPreference} to handle int using strings.
 */
@SuppressWarnings("unused")
public class IntEditTextPreference extends EditTextPreference {

    public IntEditTextPreference(@NonNull Context context) {
        super(context);
    }

    public IntEditTextPreference(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public IntEditTextPreference(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @NonNull
    @Override
    protected String getPersistedString(@NonNull String defaultReturnValue) {
        return String.valueOf(this.getPersistedInt(-1));
    }

    @Override
    protected boolean persistString(@NonNull String value) {
        return this.persistInt(Integer.valueOf(value));
    }
}
