package fr.ig2i.chat.view.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

/**
 * Base activity for activities with a repeated request.
 */
public abstract class RepeatableActivity extends BaseActivity {
    private static final int DELAY = 10 * 1000;
    @Nullable
    protected Handler handler;
    private Runnable reloadCode = new Runnable() {
        private RepeatableActivity ra = RepeatableActivity.this;

        @Override
        public void run() {
            this.ra.executeRequest();
            this.ra.handler.postDelayed(this, RepeatableActivity.DELAY);
        }
    };

    /**
     * Perform initialisation.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler = new Handler();
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.handler.post(this.reloadCode);
    }

    /**
     * Called as part of the activity lifecycle when an activity is going into
     * the background, but has not (yet) been killed.  The counterpart to
     * {@link #onResume}.
     * <p>
     * When activity B is launched in front of activity A, this callback will
     * be invoked on A.  B will not be created until A's {@link #onPause} returns,
     * so be sure to not do anything lengthy here.
     * <p>
     * This callback is mostly used for saving any persistent state the
     * activity is editing, to present a "edit in place" model to the user and
     * making sure nothing is lost if there are not enough resources to start
     * the new activity without first killing this one.  This is also a good
     * place to do things like stop animations and other things that consume a
     * noticeable amount of CPU in order to make the switch to the next activity
     * as fast as possible, or to close resources that are exclusive access
     * such as the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        this.handler.removeCallbacks(this.reloadCode);
    }

    protected abstract void executeRequest();
}
