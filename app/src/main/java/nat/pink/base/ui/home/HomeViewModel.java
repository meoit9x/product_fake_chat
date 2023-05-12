package nat.pink.base.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import nat.pink.base.R;
import nat.pink.base.base.BaseViewModel;
import nat.pink.base.model.FakeUser;
import nat.pink.base.model.ObjectLanguage;

public class HomeViewModel extends BaseViewModel {

    MutableLiveData<ArrayList<FakeUser>> languages = new MutableLiveData<>();

    public void initData(Context context) {
        ArrayList<FakeUser> fakeUsers = new ArrayList<>();


        fakeUsers.add(new FakeUser("Create \nnew", BitmapFactory.decodeResource(context.getResources(), R.drawable.add_fake_user2),false));
        fakeUsers.add(new FakeUser("Cristiano Ronaldo", BitmapFactory.decodeResource(context.getResources(), R.drawable.ronaldo),true));
        fakeUsers.add(new FakeUser("Leo \nMessi", BitmapFactory.decodeResource(context.getResources(), R.drawable.messi2),true));
        fakeUsers.add(new FakeUser("Taylor \nSwift", BitmapFactory.decodeResource(context.getResources(), R.drawable.taylor),true));
        fakeUsers.add(new FakeUser("Johnny \nDepp", BitmapFactory.decodeResource(context.getResources(), R.drawable.depp),true));
        languages.postValue(fakeUsers);
    }

}
