package nat.pink.base.ui.chat;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

//import com.applovin.mediation.MaxAd;
//import com.applovin.mediation.MaxAdListener;
//import com.applovin.mediation.MaxError;
//import com.applovin.mediation.ads.MaxInterstitialAd;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import nat.pink.base.R;
import nat.pink.base.adapter.MessageAdapter;
import nat.pink.base.dao.DatabaseController;
import nat.pink.base.databinding.FragmentChatBinding;
import nat.pink.base.dialog.DialogCreateRecord;
import nat.pink.base.dialog.DialogDeleteMessenger;
import nat.pink.base.dialog.DialogEditMessage;
import nat.pink.base.dialog.DialogImageScreenshort;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.ObjectMessenge;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Config;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.FileUtil;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.SoftInputAssist;
import nat.pink.base.utils.Toolbox;
import nat.pink.base.utils.Utils;

public class FragmentChat extends AppCompatActivity implements View.OnClickListener {

    @NonNull
//    @Override
//    public HomeViewModel getViewModel() {
//        return new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
//    }

    public static final String TAG = "FragmentChat";
    private SoftInputAssist softInputAssist;
    private DaoContact objectUser;
    private String pathImageSelect;
    private MessageAdapter messageAdapter;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 1001;

    HomeViewModel homeViewModel;
    FirebaseAnalytics mFirebaseAnalytics;
    FragmentChatBinding binding;
    private InterstitialAd interstitialAd;
    //    private MaxInterstitialAd interstitialAd;
//    private MaxNativeAdLoader nativeAdLoader;
//    private MaxNativeAdView nativeAdView;
//    private MaxAd nativeAd;
//    private MaxAdView bannerAd;
    private int retryAttempt;
    private boolean showInterstitial = false;
    private boolean showNativeAd = false;
    private Consumer interstitialConsumer;

    public FragmentChat() {
    }

//    private MaxInterstitialAd interstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentChatBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        initView();
        initData();
        initEvent();
    }

    private HomeViewModel getViewModel() {
        return homeViewModel;
    }

    public void initView() {
        softInputAssist = new SoftInputAssist(this);
        binding.tvBlock.setText(Html.fromHtml(getString(R.string.block_content), Html.FROM_HTML_MODE_COMPACT));
        binding.rcvMessage.setLayoutManager(new LinearLayoutManager(this));
        createInterstitialAd(Const.KEY_ADS_CREATE_CONTACT);
    }

    public void initEvent() {
        binding.llMessage.setOnClickListener(this);
        binding.layoutTop.imBack.setOnClickListener(this);
        binding.layoutTop.imCall.setOnClickListener(this);
        binding.layoutTop.imVideo.setOnClickListener(this);
        binding.layoutTop.imInfor.setOnClickListener(this);
        binding.layoutBottom.imMore.setOnClickListener(this);
        binding.layoutBottom.imSend.setOnClickListener(this);
        binding.layoutBottom.imPicture.setOnClickListener(this);
        binding.layoutBottom.edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setStatusItemBottom(count != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.layoutBottom.imMore.setOnClickListener(this);
        binding.layoutBottom.imEmoji.setOnClickListener(this);
        binding.layoutBottom.imSend.setOnClickListener(this);
        binding.layoutBottom.imPicture.setOnClickListener(this);
        binding.layoutBottom.imRecord.setOnClickListener(this);
        binding.layoutBottom.imTakePhoto.setOnClickListener(this);
    }

    public void initData() {
        createInterstitialAd(Const.KEY_ADMOB_INTERSTITIAL_TEST);
        Intent intent = getIntent();
        if (intent != null) {
            Object ob = intent.getExtras().get(Const.KEY_DATA_CONTACT);
            if (ob instanceof DaoContact) {
                objectUser = (DaoContact) ob;
            }
        }

        if (objectUser == null)
            return;

        loadUser();
        getViewModel().loadChatInfo.observe(this, obj -> {
            objectUser = obj;
            loadUser();
            loadData();
        });

        binding.layoutBottom.imEmoji.setOnClickListener(this);
        getViewModel().reloadDataMessenger.observe(this, aBoolean -> {
            if (aBoolean) {
                loadData();
            }
        });
        messageAdapter = new MessageAdapter(this, objectUser, getViewModel().objectMessenges);
        binding.rcvMessage.setAdapter(messageAdapter);
        messageAdapter.setItemLongClickListener((position, itemType, view) -> {
            DialogDeleteMessenger dialogDeleteMessenger = new DialogDeleteMessenger(this, R.style.MaterialDialogSheet, o -> {
                DatabaseController.getInstance(this).deleteMessenger(getViewModel().objectMessenges.get(position).getId());
                getViewModel().objectMessenges.remove(position);
                loadData();
            });
            dialogDeleteMessenger.show();
        });
        loadData();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void loadUser() {
        getViewModel().objectMessenges.clear();
        ImageUtils.loadImage(this, binding.layoutTop.imInline, objectUser.getAvatar());
        if (objectUser.getName() != null)
            binding.layoutTop.tvName.setText(objectUser.getName());
        binding.layoutTop.tvContent.setText(Utils.getStringFromIndex(this, objectUser.getOnline() - 1));
        binding.layoutTop.imStatus.setVisibility(objectUser.getOnline() == 1 ? View.VISIBLE : View.GONE);
//        binding.layoutTop.imStatusOff.setVisibility(objectUser.getOnline() == 1 ? View.GONE : View.VISIBLE);

    }

    private void loadData() {
        getViewModel().getMessengerById(this, objectUser.getId(), o -> {
            if (getViewModel().objectMessenges.isEmpty() || getViewModel().objectMessenges.get(0).getType() != Config.TYPE_HEAEDER) {
                ObjectMessenge messageModel = new ObjectMessenge();
                messageModel.setType(Config.TYPE_HEAEDER);
                getViewModel().objectMessenges.add(0, messageModel);
            }

            if (getViewModel().objectMessenges.size() > 0) {
                if (messageAdapter == null)
                    return;
                messageAdapter.setList(getViewModel().objectMessenges);
                messageAdapter.notifyDataSetChanged();
            } else {
                Log.e("natruou", "2");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (softInputAssist != null)
            softInputAssist.onResume();
    }

    @Override
    public void onDestroy() {
        if (softInputAssist != null)
            softInputAssist.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (softInputAssist != null)
            softInputAssist.onPause();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.layoutTop.imBack.getId()) {
            Utils.hideKeyboard(binding.layoutBottom.edtInput);
            finish();
        }
        if (view.getId() == binding.layoutTop.imCall.getId()) {
        }
        if (view.getId() == binding.layoutTop.imVideo.getId()) {
        }
        if (view.getId() == binding.layoutTop.imInfor.getId()) {
            showPopup(view);
        }
        if (view.getId() == binding.layoutBottom.imEmoji.getId()) {
            // todo
            Intent intentEmoji = new Intent(this, StickerActivitySticker.class);
            startActivityForResult(intentEmoji, Config.REQUEST_CODE_ACT_STICKER);
        }
        if (view.getId() == binding.layoutBottom.imMore.getId()) {
            if (!binding.layoutBottom.llOpt.isShown()) {
                setStatusItemBottom(false);
            }
        }
        if (view.getId() == binding.layoutBottom.imSend.getId()) {
            showPopupOptSend();
        }
        if (view.getId() == binding.layoutBottom.imTakePhoto.getId()) {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.ask_save_image))
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        dialog.cancel();
                        saveImage();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }
        if (view.getId() == binding.layoutBottom.imRecord.getId()) {
            new DialogCreateRecord.ExtendBuilder()
                    .setTitle(getString(R.string.voice_message))
                    .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                        Boolean isSend = (Boolean) data.get(Config.KEY_TYPE_SEND);
                        String time = (String) data.get(Config.KEY_CONTENT);
                        ObjectMessenge message = Config.createMessage(time, Config.TYPE_RECORD);
                        message.setUserOwn(objectUser.getId());
                        message.setSend(isSend);
                        getViewModel().inserMessenger(this, message);
                        getViewModel().objectMessenges.add(message);
                        scrollLastItemMessage();
                        baseDialog.dismiss();
                    })
                    .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                        baseDialog.dismiss();
                    })
                    .build()
                    .show(this.getSupportFragmentManager(), DialogEditMessage.class.getName());
        }
        if (view.getId() == binding.layoutBottom.imPicture.getId()) {
            try {
                Utils.askPermissionStorage(this, () -> {
                    Utils.requestGetGallery(this);
                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showPopup(View view) {
        Toolbox.hideSoftKeyboard(this);
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater()
                .inflate(R.menu.menu_infor_detail_conversation, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_change_info:

                    break;
                case R.id.menu_conversation:
                    finish();
                    break;
                case R.id.menu_clear_chat:
                    showInterstitialAd(o -> {
                        //TODO delete conversation here
                        getViewModel().deleteMessByOwner(getBaseContext(), objectUser.getId(), v -> {
                            if (getViewModel().objectMessenges.isEmpty() || getViewModel().objectMessenges.get(0).getType() != Config.TYPE_HEAEDER) {
                                ObjectMessenge messageModel = new ObjectMessenge();
                                messageModel.setType(Config.TYPE_HEAEDER);
                                getViewModel().objectMessenges.add(0, messageModel);
                            }
                            messageAdapter.notifyDataSetChanged();
                        });
                    });
//                    if (interstitialAd != null && interstitialAd.isReady()) {
//                        interstitialAd.showAd();
//                        return true;
//                    }
                    break;
            }
            return true;
        });
        popup.setForceShowIcon(true);
        popup.show();
    }

    private void showPopupOptSend() {
        PopupMenu popup = new PopupMenu(this, binding.layoutBottom.imSend);
        popup.getMenuInflater()
                .inflate(R.menu.menu_send_message, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            boolean isSend = item.getItemId() == R.id.menu_send;
            getViewModel().inserMessenger(this, initDataSend(isSend));
            ObjectMessenge messageModel = initDataSend(isSend);
            getViewModel().objectMessenges.add(messageModel);
            scrollLastItemMessage();
            return true;
        });
        popup.show();
    }

    public void scrollLastItemMessage() {
        if (messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
        }
        binding.rcvMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
        binding.layoutBottom.edtInput.setFocusableInTouchMode(true);
        binding.layoutBottom.edtInput.requestFocus(0);
    }

    private ObjectMessenge initDataSend(boolean isSend) {
        String data;
        int type;
        if (binding.layoutSelectImage.container.isShown()) {
            data = pathImageSelect;
            type = Config.TYPE_IMAGE;
            cleanImageAfterSend();
        } else if (!TextUtils.isEmpty(binding.layoutBottom.edtInput.getText().toString())) {
            data = binding.layoutBottom.edtInput.getText().toString();
            type = Config.TYPE_TEXT;
            cleanChatAfterSend();
        } else {
            data = Config.IC_LIKE_NAME;
            type = Config.TYPE_STICKER;
        }
        ObjectMessenge message = Config.createMessage(data, type);
        message.setSend(isSend);
        message.setUserOwn(objectUser.getId());
        return message;
    }

    public void cleanChatAfterSend() {
        binding.layoutBottom.edtInput.setText("");
    }

    public void cleanImageAfterSend() {
        binding.layoutSelectImage.container.setVisibility(View.GONE);
        pathImageSelect = "";
        if (!TextUtils.isEmpty(binding.layoutBottom.edtInput.getText().toString())) {
            binding.layoutBottom.imSend.setImageResource(R.drawable.ic_send);
        } else {
            binding.layoutBottom.imSend.setImageResource(R.drawable.ic_like);
        }
    }

    private void setStatusItemBottom(boolean isChating) {
        binding.layoutBottom.llOpt.setVisibility(isChating ? View.GONE : View.VISIBLE);
        binding.layoutBottom.imSend.setImageResource(isChating ? R.drawable.ic_send : R.drawable.ic_like);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.PICK_FROM_GALLERY && data != null && data.getData() != null) {
            CropImage.activity(data.getData())
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == -1 && null != data) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            pathImageSelect = resultUri.toString();
            binding.layoutSelectImage.container.setVisibility(View.VISIBLE);
            Glide.with(this).load(resultUri).into(binding.layoutSelectImage.imPreview);
            binding.layoutBottom.imSend.setImageResource(R.drawable.ic_send);
        }
        if (requestCode == Config.REQUEST_CODE_ACT_STICKER) {
            if (data == null) {
                return;
            }
            String resName = data.getStringExtra(Config.KEY_CONTENT);
            boolean isSend = data.getBooleanExtra(Config.KEY_TYPE_SEND, true);
            ObjectMessenge messageModel = Config.createMessage(resName, Config.TYPE_STICKER);
            messageModel.setSend(isSend);
            messageModel.setUserOwn(objectUser.getId());
            getViewModel().inserMessenger(this, messageModel);
            getViewModel().objectMessenges.add(messageModel);
            scrollLastItemMessage();
        }
    }

    public void saveImage() {
        try {
            askPermissionStorage(() -> {
                File file = FileUtil.createFolder(this, getString(R.string.app_name));
                String path = FileUtil.saveImageToGallery(this
                        , file
                        , Toolbox.screenShortView(binding.container));
                new DialogImageScreenshort.ExtendBuilder()
                        .setPath(path)
                        .setTitle(getString(R.string.screenshort_success))
                        .build().show(this.getSupportFragmentManager(), DialogImageScreenshort.class.getName());
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void askPermissionStorage(Callable<Void> callable) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
            } else {
                callable.call();
            }
        } else {
            callable.call();
        }
    }
    private void setCallbackInterstitial() {
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.");
                interstitialAd = null;
                interstitialConsumer.accept(null);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.");
                interstitialAd = null;
            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.");
            }
        });
    }
    public void createInterstitialAd(String keyAds) {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, keyAds, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd mInterstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                interstitialAd = mInterstitialAd;
                setCallbackInterstitial();
                updateAdsRequest();
                Log.i(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.d(TAG, loadAdError.toString());
                interstitialAd = null;
                // createInterstitialAd(keyAds);
            }
        });

//        if (interstitialAd == null || interstitialAd.getAdUnitId() != keyAds) {
//            interstitialAd = new MaxInterstitialAd(keyAds, this);
//            interstitialAd.setListener(new MaxAdListener() {
//                @Override
//                public void onAdLoaded(MaxAd maxAd) {
//                    retryAttempt = 0;
//                    //  Toast.makeText(MainActivity.this, "ad loaded", Toast.LENGTH_SHORT).show();
//                    Log.d("adsDebug", "onAdLoaded: ");
//                }
//
//                @Override
//                public void onAdDisplayed(MaxAd maxAd) {
//                    Log.d("adsDebug", "onAdDisplayed: ");
//                    //   Toast.makeText(MainActivity.this, "ad displayed", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onAdHidden(MaxAd maxAd) {
//                    interstitialAd.loadAd();
//                    getViewModel().deleteMessByOwner(getBaseContext(), objectUser.getId(), v -> {
//                        if (getViewModel().objectMessenges.isEmpty() || getViewModel().objectMessenges.get(0).getType() != Config.TYPE_HEAEDER) {
//                            ObjectMessenge messageModel = new ObjectMessenge();
//                            messageModel.setType(Config.TYPE_HEAEDER);
//                            getViewModel().objectMessenges.add(0, messageModel);
//                        }
//                        messageAdapter.notifyDataSetChanged();
//                    });
//                }
//
//                @Override
//                public void onAdClicked(MaxAd maxAd) {
//                    mFirebaseAnalytics.logEvent("ClickRefreshChat", null);
//                }
//
//                @Override
//                public void onAdLoadFailed(String s, MaxError maxError) {
//                    // Interstitial ad failed to load
//                    // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
//                    retryAttempt++;
//                    long delayMillis = TimeUnit.SECONDS.toMillis(5);
//
//                    new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
//                }
//
//                @Override
//                public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
//                    interstitialAd.loadAd();
//                }
//            });
//        }
//        // Load the first ad
//        interstitialAd.loadAd();
    }

    protected void updateAdsRequest() {
//        if (showInterstitial && interstitialAd != null && interstitialAd.isReady()) {
//            setLoadingAdsView(false);
//            interstitialAd.showAd();
//            showInterstitial = false;
//        }
//        if (showNativeAd && nativeAdView != null && nativeAd != null) {
//            nativeAdLoader.render(nativeAdView, nativeAd);
//            if (nativeAdLoadedConsumer != null) {
//                nativeAdLoadedConsumer.accept(nativeAdView);
//            }
//            showNativeAd = false;
//        }

        if (showInterstitial && interstitialAd != null) {
            setLoadingAdsView(false);
            interstitialAd.show(this);
            showInterstitial = false;
        }
    }

    public boolean showInterstitialAd(Consumer doneConsumer) {
        interstitialConsumer = doneConsumer;
//        if (interstitialAd != null && interstitialAd.isReady()) {
//            interstitialAd.showAd();
//            return true;
//        }
        if (interstitialAd != null) {
            interstitialAd.show(this);
            return true;
        }
        createInterstitialAd(Const.KEY_ADMOB_INTERSTITIAL_TEST);
        setLoadingAdsView(true);
        showInterstitial = true;
        return false;
    }

    public void setLoadingAdsView(Boolean visible) {
        Log.d(TAG, "LoadingAdsView: " + visible);
        binding.loadingAdsLayout.loadingAdsLayout.bringToFront();
        binding.loadingAdsLayout.loadingAdsLayout.setVisibility(View.VISIBLE == binding.loadingAdsLayout.loadingAdsLayout.getVisibility() ? View.GONE : View.VISIBLE);
        binding.container.setVisibility(View.VISIBLE == binding.container.getVisibility() ? View.GONE : View.VISIBLE);
    }
}
