package nat.pink.base.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import nat.pink.base.model.DaoContact;

@Database(entities = {DaoContact.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context,
                            AppDatabase.class,
                            "Word-Database")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }

    public abstract ContactDao getContactDao();
}