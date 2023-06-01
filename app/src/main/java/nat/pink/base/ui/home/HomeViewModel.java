package nat.pink.base.ui.home;

import android.content.Context;

import androidx.core.util.Consumer;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nat.pink.base.base.BaseViewModel;
import nat.pink.base.dao.DatabaseController;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.ObjectMessenge;

public class HomeViewModel extends BaseViewModel {

    public MutableLiveData<List<DaoContact>> contacts = new MutableLiveData<>();
    public MutableLiveData<List<DaoContact>> contactSuggest = new MutableLiveData<>();
    public MutableLiveData<List<DaoContact>> contactNormal = new MutableLiveData<>();
    public List<ObjectMessenge> objectMessenges = new ArrayList<>();
    public MutableLiveData<Boolean> reloadDataMessenger = new MutableLiveData<>();
    public MutableLiveData<DaoContact> loadChatInfo = new MutableLiveData<>();

    public void getListContact(Context context) {
        List<DaoContact> daoContacts = DatabaseController.getInstance(context).getContact();
        List<DaoContact> _contactactSuggest = new ArrayList<>();
        List<DaoContact> _contactactNormal = new ArrayList<>();
        for (int i = 0; i < daoContacts.size(); i++) {
            if (i <= 3) {
                _contactactSuggest.add(daoContacts.get(i));
            }else{
                _contactactNormal.add(daoContacts.get(i));
            }
        }
        Collections.sort(daoContacts, DaoContact.Comparators.NAME);
        contacts.postValue(daoContacts);
        contactSuggest.postValue(_contactactSuggest);
        contactNormal.postValue(_contactactNormal);
    }

    public void insertContact(Context context, DaoContact contact) {
        DatabaseController.getInstance(context).insertContact(contact);
        getListContact(context);
    }

    public void getMessengerById(Context context, int userId, Consumer consumer) {
        objectMessenges = DatabaseController.getInstance(context).getMessageById(userId);
        consumer.accept(new Object());
    }

    public boolean inserMessenger(Context context, ObjectMessenge objectMessenge) {
        if (DatabaseController.getInstance(context).insertMessenge(objectMessenge) != 0) {
            reloadDataMessenger.postValue(true);
            return true;
        }
        return false;
    }

    public void deleteContact(Context context, DaoContact objectMessenge) {
        DatabaseController.getInstance(context).deleteContact(objectMessenge);
        getListContact(context);
    }

    public void updateContact(Context context, DaoContact objectMessenge) {
        DatabaseController.getInstance(context).updateContact(objectMessenge);
        getListContact(context);
    }
}
