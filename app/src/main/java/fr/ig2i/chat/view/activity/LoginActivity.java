package fr.ig2i.chat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import fr.ig2i.chat.R;
import fr.ig2i.chat.asynctask.RequestAsyncTask;
import fr.ig2i.chat.asynctask.RequestAsyncTaskCallback;
import fr.ig2i.chat.manager.PreferencesManager;
import fr.ig2i.chat.model.parameter.LoginParameters;
import fr.ig2i.chat.model.result.LoginResult;
import retrofit2.Call;

/**
 * Login screen.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String ARG_ERROR_FROM_BEFORE = "ARG_ERROR_FROM_BEFORE";
    protected static final String TAG = LoginActivity.class.getSimpleName();
    @Nullable
    protected EditText loginField;
    @Nullable
    protected EditText passwordField;
    @Nullable
    protected CheckBox rememberField;
    @Nullable
    protected Button validationButton;
    @Nullable
    protected ProgressBar progressBar;

    private boolean hasErrorFromBefore = false;

    private RequestAsyncTaskCallback<LoginResult> callbackLoginAsyncTask = new RequestAsyncTaskCallback<LoginResult>() {

        private final LoginActivity la = LoginActivity.this;

        @Override
        public void onSuccess(@NonNull LoginResult result) {
            this.la.changeFields(true);

            String token = result.getToken();

            if (TextUtils.isEmpty(token)) {
                result.setStatus("Invalid token");
                this.onError(result);
                return;
            }

            // Store token
            if (this.la.globalState != null) {
                this.la.globalState.getPreferencesManager().setToken("Bearer " + token);
            }

            String username = this.la.loginField.getText().toString();
            String password = this.la.passwordField.getText().toString();
            boolean remember = this.la.rememberField.isChecked();

            this.la.globalState.getPreferencesManager().setCredentials(result.getId(), username, password, remember);
            this.la.startActivity(new Intent(LoginActivity.this, ListChatActivity.class));
            this.la.finish();
        }

        @Override
        public void onError(@Nullable LoginResult result) {
            this.la.changeFields(true);
            Log.e(LoginActivity.TAG, "Non successful response");

            if (result != null) {
                this.la.loginField.setError(result.getStatus());
            } else {
                this.la.loginField.setError("Unknown error");
            }

            this.la.loginField.requestFocus();
        }
    };

    /**
     * Perform initialisation.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);

        if (this.getIntent() != null) {
            this.hasErrorFromBefore = this.getIntent().getBooleanExtra(LoginActivity.ARG_ERROR_FROM_BEFORE, false);
        }

        this.loginField = this.findViewById(R.id.activity_login_username_field);
        this.passwordField = this.findViewById(R.id.activity_login_password_field);
        this.rememberField = this.findViewById(R.id.activity_login_remember_field);
        this.validationButton = this.findViewById(R.id.activity_login_validation_field);
        this.progressBar = this.findViewById(R.id.activity_login_progress_bar);

        this.validationButton.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.activity_login_validation_field:
                this.onFieldSubmit((Button) v);
                break;
        }
    }

    /**
     * Called after {@link #onCreate} - or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     */
    @Override
    protected void onStart() {
        super.onStart();

        PreferencesManager pm = this.globalState.getPreferencesManager();

        if (!TextUtils.isEmpty(pm.getToken()) && !this.hasErrorFromBefore) {
            // Auto-login if we have a token and we haven't got an error before
            this.startActivity(new Intent(LoginActivity.this, ListChatActivity.class));
            this.finish();
            return;
        }

        this.loginField.setText(pm.getUsername());
        this.passwordField.setText(pm.getPassword());
        this.rememberField.setChecked(pm.getRemember());

        if (this.hasErrorFromBefore) {
            Snackbar.make(this.findViewById(R.id.LoginCoordinatorLayout), R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!this.globalState.checkForNetworkConnectivity()) {
            Snackbar.make(this.findViewById(R.id.LoginCoordinatorLayout), R.string.no_network, Snackbar.LENGTH_LONG)
                    .show();
        }
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
        menu.removeItem(R.id.action_account);
        menu.removeItem(R.id.action_logout);
        return true;
    }

    protected void changeFields(boolean enabled) {
        this.loginField.setEnabled(enabled);
        this.passwordField.setEnabled(enabled);
        this.rememberField.setEnabled(enabled);
        this.validationButton.setEnabled(enabled);
        if (enabled) {
            this.progressBar.setVisibility(View.INVISIBLE);
        } else {
            this.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @SuppressWarnings("unchecked")
    protected void onFieldSubmit(@NonNull Button b) {
        String username = this.loginField.getText().toString();
        String password = this.passwordField.getText().toString();
        boolean remember = this.rememberField.isChecked();

        this.changeFields(false);
        Call<LoginResult> requestCall = this.globalState.getApiRepository().login(new LoginParameters(username, password));

        // Start request
        new RequestAsyncTask<>(this.callbackLoginAsyncTask).execute(requestCall);
    }

}
