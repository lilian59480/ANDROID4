package fr.ig2i.chat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fr.ig2i.chat.IG2IChatApplication;
import fr.ig2i.chat.R;

/**
 * Base activity for common values shared.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    protected IG2IChatApplication globalState;

    /**
     * Perform initialisation.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.globalState = (IG2IChatApplication) this.getApplication();
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                this.onOptionsSettingsSelected();
                break;
            case R.id.action_account:
                this.onOptionsAccountSelected();
                break;
            case R.id.action_logout:
                this.onOptionsLogoutSelected();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Option callback.
     */
    public void onOptionsSettingsSelected() {
        this.startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * Option callback.
     */
    public void onOptionsAccountSelected() {
        this.startActivity(new Intent(this, AccountActivity.class));
    }

    /**
     * Option callback.
     */
    public void onOptionsLogoutSelected() {
        this.globalState.getPreferencesManager().setToken(null);
        Intent i = new Intent(this, LoginActivity.class);
        // Remove all activities to the stack so we can't go back
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(i);
    }

    /**
     * Internal or network error which invalidate token.
     */
    public void errorLogout() {
        this.globalState.getPreferencesManager().setToken(null);
        Intent i = new Intent(this, LoginActivity.class);
        // Remove all activities to the stack so we can't go back
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(LoginActivity.ARG_ERROR_FROM_BEFORE, true);
        this.startActivity(i);
    }

}
