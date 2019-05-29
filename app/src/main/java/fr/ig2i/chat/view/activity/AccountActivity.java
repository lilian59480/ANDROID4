package fr.ig2i.chat.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import fr.ig2i.chat.R;
import fr.ig2i.chat.asynctask.RequestAsyncTask;
import fr.ig2i.chat.asynctask.RequestAsyncTaskCallback;
import fr.ig2i.chat.model.User;
import fr.ig2i.chat.model.result.Result;
import fr.ig2i.chat.model.result.UserResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Display and modify the current account information.
 */
public class AccountActivity extends BaseActivity implements View.OnClickListener {

    @Nullable
    protected EditText loginField;
    @Nullable
    protected EditText passwordField;
    @Nullable
    protected AutoCompleteTextView colourField;
    @Nullable
    protected Button validationButton;
    @Nullable
    protected ProgressBar progressBar;

    private Callback<UserResult> requestUser = new Callback<UserResult>() {
        private final AccountActivity aa = AccountActivity.this;

        private void error(@StringRes int resId) {
            Log.e(ListChatActivity.TAG, "Non successful response");
        }

        @Override
        public void onResponse(@NonNull Call<UserResult> call, @NonNull Response<UserResult> response) {
            if (!response.isSuccessful()) {
                this.error(R.string.network_error);
                return;
            }

            UserResult ur = response.body();

            if (ur == null) {
                this.error(R.string.network_error);
                return;
            }

            User u = ur.getUser();

            if (u == null) {
                this.error(R.string.network_error);
                return;
            }

            // Set new list
            this.aa.loginField.setText(u.getUsername());
            this.aa.colourField.setText(u.getColour());
        }

        @Override
        public void onFailure(@NonNull Call<UserResult> call, @NonNull Throwable t) {
            Log.e(LoginActivity.TAG, "Failure", t);
            this.error(R.string.network_error);
        }
    };

    private RequestAsyncTaskCallback<Result> updateUser = new RequestAsyncTaskCallback<Result>() {

        private final AccountActivity aa = AccountActivity.this;

        @Override
        public void onSuccess(@NonNull Result result) {
            this.aa.changeFields(true);

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

            String username = this.aa.loginField.getText().toString();
            String password = this.aa.passwordField.getText().toString();
            boolean remember = this.aa.globalState.getPreferencesManager().getRemember();
            int userId = this.aa.globalState.getPreferencesManager().getUserId();

            this.aa.globalState.getPreferencesManager().setCredentials(userId, username, password, remember);
            this.aa.finish();
        }

        @Override
        public void onError(@Nullable Result result) {
            this.aa.changeFields(true);
            Log.e(LoginActivity.TAG, "Non successful response");
            this.aa.errorLogout();
        }
    };


    /**
     * Perform initialisation.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_account);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.colors, R.layout.fragment_dropdown_menu_popup_item);

        this.loginField = this.findViewById(R.id.activity_account_username_field);
        this.passwordField = this.findViewById(R.id.activity_account_password_field);
        this.colourField = this.findViewById(R.id.activity_account_colours_field);
        this.colourField.setAdapter(adapter);
        this.validationButton = this.findViewById(R.id.activity_account_validation_field);


        this.progressBar = this.findViewById(R.id.activity_account_progress_bar);

        this.validationButton.setOnClickListener(this);
    }

    /**
     * Called after {@link #onCreate} - or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     */
    @Override
    protected void onStart() {
        super.onStart();
        this.loginField.setText(this.globalState.getPreferencesManager().getUsername());
        this.passwordField.setText(this.globalState.getPreferencesManager().getPassword());
        this.globalState.getApiRepository().getUser(this.globalState.getPreferencesManager().getToken(), this.globalState.getPreferencesManager().getUserId()).enqueue(this.requestUser);
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
        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.activity_account_validation_field:
                this.onFieldSubmit((Button) v);
                break;
        }
    }

    protected void changeFields(boolean enabled) {
        this.loginField.setEnabled(enabled);
        this.passwordField.setEnabled(enabled);
        this.colourField.setEnabled(enabled);
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
        String colour = this.colourField.getText().toString();

        boolean invalidate = false;

        User mergedUser = new User();
        if (!TextUtils.isEmpty(username)) {
            mergedUser.setUsername(username);
            invalidate = true;
        }
        if (!TextUtils.isEmpty(password)) {
            mergedUser.setPassword(password);
            invalidate = true;
        }
        if (!TextUtils.isEmpty(colour)) {
            mergedUser.setColour(colour);
        }
        mergedUser.setConnected(true);

        this.changeFields(false);
        Call<Result> updateCall = this.globalState.getApiRepository().updateUser(this.globalState.getPreferencesManager().getToken(), this.globalState.getPreferencesManager().getUserId(), mergedUser);

        // Start request
        new RequestAsyncTask<>(this.updateUser).execute(updateCall);
    }
}
