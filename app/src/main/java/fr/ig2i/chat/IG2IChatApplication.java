package fr.ig2i.chat;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.CookieHandler;
import java.net.CookieManager;

import fr.ig2i.chat.api.ApiRepository;
import fr.ig2i.chat.api.ApiService;
import fr.ig2i.chat.manager.PreferencesManager;

/**
 * Base class for maintaining global application state. You can provide your own
 * implementation by creating a subclass and specifying the fully-qualified name
 * of this subclass as the <code>"android:name"</code> attribute in your
 * AndroidManifest.xml's <code>&lt;application&gt;</code> tag. The Application
 * class, or your subclass of the Application class, is instantiated before any
 * other class when the process for your application/package is created.
 */
public class IG2IChatApplication extends Application {

    private static final String TAG = IG2IChatApplication.class.getSimpleName();

    private PreferencesManager pm;

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * <p>
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        this.pm = new PreferencesManager(this);
    }

    public boolean checkForNetworkConnectivity() {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        ConnectivityManager cnMngr = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cnMngr.getActiveNetworkInfo();

        String sType = "Aucun réseau détecté";
        boolean bStatut = false;
        if (netInfo != null) {

            NetworkInfo.State netState = netInfo.getState();

            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true;
                int netType = netInfo.getType();
                switch (netType) {
                    case ConnectivityManager.TYPE_MOBILE:
                        sType = "Réseau mobile détecté";
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        sType = "Réseau wifi détecté";
                        break;
                }

            }
        }
        Log.i(TAG, sType);
        return bStatut;
    }

    @NonNull
    public PreferencesManager getPreferencesManager() {
        return this.pm;
    }

    @Nullable
    public ApiService getApiRepository() {
        return ApiRepository.getApiService(this);
    }
}
