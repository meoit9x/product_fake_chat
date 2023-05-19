package nat.pink.base.ui.home;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import nat.pink.base.R;
import nat.pink.base.base.BaseViewModel;
import nat.pink.base.model.ObjectUser;

public class HomeViewModel extends BaseViewModel {

    MutableLiveData<ArrayList<ObjectUser>> users = new MutableLiveData<>();

    public void initData(Context context) {
        ArrayList<ObjectUser> fakeUsers = new ArrayList<>();
        fakeUsers.add(new ObjectUser(1,"Create \nnew", "", 0, Uri.parse("android.resource://"+context.getPackageName()+"/drawable/add_fake_user2").toString()));
        fakeUsers.add(new ObjectUser(2,"Cristiano Ronaldo", "", 1, Uri.parse("android.resource://"+context.getPackageName()+"/drawable/ronaldo").toString()));
        fakeUsers.add(new ObjectUser(3,"Leo \nMessi", "", 1,Uri.parse("android.resource://"+context.getPackageName()+"/drawable/messi2").toString()));
        fakeUsers.add(new ObjectUser(4,"Taylor \nSwift", "", 1,Uri.parse("android.resource://"+context.getPackageName()+"/drawable/taylor").toString()));
        fakeUsers.add(new ObjectUser(5,"Johnny \nDepp", "", 1,Uri.parse("android.resource://"+context.getPackageName()+"/drawable/depp").toString()));
        users.postValue(fakeUsers);
    }

}
