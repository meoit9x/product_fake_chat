package nat.pink.base.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import nat.pink.base.base.BaseViewModel;
import nat.pink.base.dao.DatabaseController;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.DeviceRequest;
import nat.pink.base.model.FeedbackRequest;
import nat.pink.base.model.ObjectMessenge;
import nat.pink.base.model.ResponseDevice;
import nat.pink.base.model.ResponseFeedback;
import nat.pink.base.model.ResponseLeaderBoard;
import nat.pink.base.model.ResponseUpdatePoint;
import nat.pink.base.model.UpdatePointRequest;
import nat.pink.base.retrofit.RequestAPI;
import nat.pink.base.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends BaseViewModel {

    public MutableLiveData<List<DaoContact>> contacts = new MutableLiveData<>();
    public MutableLiveData<List<DaoContact>> contactSuggest = new MutableLiveData<>();
    public MutableLiveData<List<DaoContact>> contactNormal = new MutableLiveData<>();
    public List<ObjectMessenge> objectMessenges = new ArrayList<>();
    public MutableLiveData<Boolean> reloadDataMessenger = new MutableLiveData<>();
    public MutableLiveData<DaoContact> loadChatInfo = new MutableLiveData<>();
    public MutableLiveData<ResponseDevice> totalCoin = new MutableLiveData<>();
    public MutableLiveData<ResponseUpdatePoint> updateCoin = new MutableLiveData<>();
    public MutableLiveData<Boolean> forceUpdate = new MutableLiveData<>();
    public MutableLiveData<String> typeAds = new MutableLiveData<>();
    public final MutableLiveData<String> failed = new MutableLiveData<>();

    public void getListContact(Context context) {
        List<DaoContact> daoContacts = DatabaseController.getInstance(context).getContact();
        List<DaoContact> _contactactSuggest = new ArrayList<>();
        List<DaoContact> _contactactNormal = new ArrayList<>();
        for (int i = 0; i < daoContacts.size(); i++) {
            if (i <= 3) {
                _contactactSuggest.add(daoContacts.get(i));
            } else {
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

    public void deleteMessByOwner(Context context, int owner, Consumer consumer) {
        DatabaseController.getInstance(context).deleteMessengerByOwner(owner);
        objectMessenges.clear();
        consumer.accept(new Object());
    }

    public void deleteContact(Context context, DaoContact objectMessenge) {
        DatabaseController.getInstance(context).deleteContact(objectMessenge);
        getListContact(context);
    }

    public void updateContact(Context context, DaoContact objectMessenge) {
        DatabaseController.getInstance(context).updateContact(objectMessenge);
        getListContact(context);
    }

    public void feedback(RequestAPI requestAPI, String pack, String version, String contentFeedback, Consumer<Object> consumer) {
        FeedbackRequest feedbackRequest = new FeedbackRequest(pack, version, contentFeedback);
        requestAPI.feedback(feedbackRequest).enqueue(new Callback<ResponseFeedback>() {
            @Override
            public void onResponse(Call<ResponseFeedback> call, Response<ResponseFeedback> response) {
                consumer.accept(response.body());
            }

            @Override
            public void onFailure(Call<ResponseFeedback> call, Throwable t) {
                consumer.accept(t);
            }
        });
    }

    public void getPoint(RequestAPI requestAPI, String deviceId) {
        DeviceRequest deviceRequest = new DeviceRequest(deviceId);
        requestAPI.getPoint(deviceRequest).enqueue(new Callback<ResponseDevice>() {
            @Override
            public void onResponse(Call<ResponseDevice> call, Response<ResponseDevice> response) {
                totalCoin.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseDevice> call, Throwable t) {
                failed.postValue("Lỗi: " + t.getMessage());
            }
        });
    }

    public void updatePoint(RequestAPI requestAPI, String deviceId,int updateType, int adjustPoint) {
        UpdatePointRequest updatePointRequest = new UpdatePointRequest(deviceId, updateType, adjustPoint);
        requestAPI.updatePoint(updatePointRequest).enqueue(new Callback<ResponseUpdatePoint>() {
            @Override
            public void onResponse(Call<ResponseUpdatePoint> call, Response<ResponseUpdatePoint> response) {
                updateCoin.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseUpdatePoint> call, Throwable t) {
                failed.postValue("Lỗi: " + t.getMessage());
            }
        });
    }

    public void getLeaderBoard(RequestAPI requestAPI, Consumer<Object> consumer) {
        requestAPI.getLeaderBoard().enqueue(new Callback<ResponseLeaderBoard>() {
            @Override
            public void onResponse(Call<ResponseLeaderBoard> call, Response<ResponseLeaderBoard> response) {
                consumer.accept(response.body());
            }

            @Override
            public void onFailure(Call<ResponseLeaderBoard> call, Throwable t) {
                consumer.accept(t);
            }
        });
    }


    public void foreUpdate(DatabaseReference databaseReference, Context context) {
        databaseReference.child("config/force").get().addOnCompleteListener(task -> {
            String value = task.getResult().getValue().toString();
            if (value.equals("true")) {
                databaseReference.child("config/ver").get().addOnCompleteListener(task1 -> {
                    String ver = String.valueOf(task1.getResult().getValue());
                    ver = ver.replace(".", "");
                    String verNow = Utils.getVer(context);
                    verNow = verNow.replace(".", "");
                    forceUpdate.postValue(Integer.parseInt(ver) < Integer.parseInt(verNow));
                });
            } else {
                forceUpdate.postValue(false);
            }
        });
    }

    public static void getAdsType(DatabaseReference databaseReference, Consumer<String> consumer) {
        databaseReference.child("config/type_ads").get().addOnCompleteListener(task -> {
            String value = task.getResult().getValue().toString();
            consumer.accept(value);
        });
    }
}
