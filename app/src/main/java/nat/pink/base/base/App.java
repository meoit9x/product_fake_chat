package nat.pink.base.base;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    private static App sInstance = null;
    private DatabaseReference firebaseDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static App getInstance() {
        return sInstance;
    }

    // type 0 = admob
    // type 1 = applovin
    private String typeAds;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public DatabaseReference getFirebaseDatabase() {
        return firebaseDatabase;
    }
    public FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    public String getTypeAds() {
        return typeAds;
    }

    public void setTypeAds(String typeAds) {
        this.typeAds = typeAds;
    }
}
