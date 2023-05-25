package nat.pink.base.ui.home;

import static nat.pink.base.utils.PreferenceUtil.KEY_SETUP_DATA_DEFAULT;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.base.BaseViewModel;
import nat.pink.base.dao.DatabaseController;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.ObjectUser;
import nat.pink.base.utils.PreferenceUtil;

public class HomeViewModel extends BaseViewModel {

    MutableLiveData<List<DaoContact>> contacts = new MutableLiveData<>();

    public void getListContact(Context context) {
        contacts.postValue(DatabaseController.getInstance(context).getContact());
    }

}
