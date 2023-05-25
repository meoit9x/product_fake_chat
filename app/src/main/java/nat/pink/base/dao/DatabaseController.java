package nat.pink.base.dao;

import static nat.pink.base.utils.PreferenceUtil.KEY_SETUP_DATA_DEFAULT;

import android.content.Context;
import java.util.List;
import nat.pink.base.R;
import nat.pink.base.model.DaoContact;
import nat.pink.base.utils.PreferenceUtil;

public class DatabaseController {

    private static DatabaseController databaseController;
    private AppDatabase appDatabase;
    private Context context;


    public DatabaseController(Context context) {
        appDatabase = AppDatabase.getInstance(context);
        this.context = context;
    }

    public static DatabaseController getInstance(Context context) {
        if (databaseController == null) {
            databaseController = new DatabaseController(context);
        }
        return databaseController;
    }

    public Long insertContact(DaoContact daoContact) {
        if (appDatabase != null) {
            return appDatabase.getContactDao().insertContact(daoContact);
        }
        return 0L;
    }

    public int updateContact(DaoContact daoContact) {
        if (appDatabase != null)
            return appDatabase.getContactDao().updateContact(daoContact);
        return 0;
    }

    public List<DaoContact> getContact() {
        if (appDatabase != null)
            return appDatabase.getContactDao().getAllContact();
        return null;
    }


    /**
     * online = 0 offline
     * online = 1 online
     * online = 2 5M
     * online = 3 30M
     * online = 4 1H
     * online = 5 1D
     */
    public void setupDataDefault() {
        DaoContact daoContact = new DaoContact();
        daoContact.setName(context.getString(R.string.data_default_name_1));
        daoContact.setOnline(1);
        daoContact.setVerified(true);
        daoContact.setIs_friend(true);
        daoContact.setColor(0);
        daoContact.setEducation("");
        daoContact.setWork(context.getString(R.string.data_default_work_1));
        daoContact.setLive(context.getString(R.string.data_default_live_1));
        daoContact.setAvatar("R.drawable.ronaldo");
        insertContact(daoContact);

        daoContact = new DaoContact();
        daoContact.setName(context.getString(R.string.data_default_name_2));
        daoContact.setOnline(1);
        daoContact.setVerified(true);
        daoContact.setIs_friend(true);
        daoContact.setColor(0);
        daoContact.setEducation(context.getString(R.string.data_default_education_2));
        daoContact.setWork(context.getString(R.string.data_default_work_2));
        daoContact.setLive(context.getString(R.string.data_default_live_2));
        daoContact.setAvatar("R.drawable.messi2");
        DatabaseController.getInstance(context).insertContact(daoContact);

        daoContact = new DaoContact();
        daoContact.setName(context.getString(R.string.data_default_name_3));
        daoContact.setOnline(1);
        daoContact.setVerified(true);
        daoContact.setIs_friend(true);
        daoContact.setColor(0);
        daoContact.setEducation("");
        daoContact.setWork(context.getString(R.string.data_default_work_3));
        daoContact.setAvatar("R.drawable.taylor");
        daoContact.setLive(context.getString(R.string.data_default_live_3));
        DatabaseController.getInstance(context).insertContact(daoContact);

        daoContact = new DaoContact();
        daoContact.setName(context.getString(R.string.data_default_name_4));
        daoContact.setOnline(1);
        daoContact.setVerified(true);
        daoContact.setIs_friend(true);
        daoContact.setColor(0);
        daoContact.setEducation("");
        daoContact.setAvatar("R.drawable.depp");
        daoContact.setWork(context.getString(R.string.data_default_work_4));
        daoContact.setLive(context.getString(R.string.data_default_live_4));
        DatabaseController.getInstance(context).insertContact(daoContact);

        PreferenceUtil.saveBoolean(context, KEY_SETUP_DATA_DEFAULT, false);
    }
}
