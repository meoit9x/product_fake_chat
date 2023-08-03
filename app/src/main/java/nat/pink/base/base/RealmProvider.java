package nat.pink.base.base;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import nat.pink.base.model.RealmMessengerModule;

public class RealmProvider {
    private RealmConfiguration realmConfiguration;

    public RealmProvider(RealmConfiguration realmConfiguration) {
        this.realmConfiguration = realmConfiguration;
    }

    private Realm realm = Realm.getInstance(realmConfiguration);

    public Realm getRealm() {
        return realm;
    }

    private static RealmConfiguration fakeMessengereConfig = new RealmConfiguration.Builder()
            .name("FakeChat.realm")
            .schemaVersion(2)
            .modules(new RealmMessengerModule())
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build();

    public static Realm realmMessenger = Realm.getInstance(fakeMessengereConfig);
}
