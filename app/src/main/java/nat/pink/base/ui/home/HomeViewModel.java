package nat.pink.base.ui.home;

import androidx.lifecycle.MutableLiveData;

import nat.pink.base.base.BaseViewModel;

public class HomeViewModel extends BaseViewModel {

    public MutableLiveData<String> loadData = new MutableLiveData<>();

    public void loadData() {
        loadData.postValue("test");
    }

}
