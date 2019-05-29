package fr.ig2i.chat.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import fr.ig2i.chat.R;

/**
 * Shows a hierarchy of {@link Preference} objects as
 * lists. These preferences will
 * automatically save to {@link android.content.SharedPreferences} as the user interacts with
 * them. To retrieve an instance of {@link android.content.SharedPreferences} that the
 * preference hierarchy in this fragment will use, call
 * {@link PreferenceManager#getDefaultSharedPreferences(android.content.Context)}
 * with a context in the same package as this fragment.
 * <p>
 * Furthermore, the preferences shown will follow the visual style of system
 * preferences. It is easy to create a hierarchy of preferences (that can be
 * shown on multiple screens) via XML. For these reasons, it is recommended to
 * use this fragment (as a superclass) to deal with preferences in applications.
 * <p>
 * A {@link PreferenceScreen} object should be at the top of the preference
 * hierarchy. Furthermore, subsequent {@link PreferenceScreen} in the hierarchy
 * denote a screen break--that is the preferences contained within subsequent
 * {@link PreferenceScreen} should be shown on another screen. The preference
 * framework handles this by calling {@link #onNavigateToScreen(PreferenceScreen)}.
 * <p>
 * The preference hierarchy can be formed in multiple ways:
 * <li> From an XML file specifying the hierarchy
 * <li> From different {@link android.app.Activity Activities} that each specify its own
 * preferences in an XML file via {@link android.app.Activity} meta-data
 * <li> From an object hierarchy rooted with {@link PreferenceScreen}
 * <p>
 * To inflate from XML, use the {@link #addPreferencesFromResource(int)}. The
 * root element should be a {@link PreferenceScreen}. Subsequent elements can point
 * to actual {@link Preference} subclasses. As mentioned above, subsequent
 * {@link PreferenceScreen} in the hierarchy will result in the screen break.
 * <p>
 * To specify an object hierarchy rooted with {@link PreferenceScreen}, use
 * {@link #setPreferenceScreen(PreferenceScreen)}.
 * <p>
 * As a convenience, this fragment implements a click listener for any
 * preference in the current hierarchy, see
 * {@link #onPreferenceTreeClick(Preference)}.
 *
 * @see Preference
 * @see PreferenceScreen
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    
    /**
     * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment.
     * Subclasses are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either
     * directly or via helper methods such as {@link #addPreferencesFromResource(int)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the
     *                           {@link androidx.preference.PreferenceScreen} with this key.
     */
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @NonNull String rootKey) {
        this.setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}

