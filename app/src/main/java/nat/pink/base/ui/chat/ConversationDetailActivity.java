package nat.pink.base.ui.chat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nat.pink.base.R;
import nat.pink.base.adapter.MessageAdapter;
import nat.pink.base.adapter.MessageGroupAdapter;
import nat.pink.base.base.BaseActivityForFragment;
import nat.pink.base.databinding.FragmentChatBinding;
import nat.pink.base.databinding.LayoutDetailBottomBinding;
import nat.pink.base.databinding.LayoutDetailSelectImageBinding;
import nat.pink.base.databinding.LayoutDetailTopBinding;
import nat.pink.base.dialog.DialogCreateRecord;
import nat.pink.base.dialog.DialogEditMessage;
import nat.pink.base.dialog.DialogImageScreenshort;
import nat.pink.base.model.ConversationModel;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.MessageModel;
import nat.pink.base.model.ObjectMessenge;
import nat.pink.base.model.UserMessageModel;
import nat.pink.base.ui.dialog.DialogSelectMember;
import nat.pink.base.ui.home.HomeViewModel;
import nat.pink.base.utils.Config;
import nat.pink.base.utils.Const;
import nat.pink.base.utils.FileUtil;
import nat.pink.base.utils.ImageUtils;
import nat.pink.base.utils.SoftInputAssist;
import nat.pink.base.utils.Toolbox;
import nat.pink.base.utils.Utils;


public class ConversationDetailActivity extends BaseActivityForFragment implements IActionDetail.IView, View.OnClickListener {

    public static final String DATA_SELECT = "data select";

    private FragmentChatBinding binding;
    private LayoutDetailTopBinding topBinding;
    private LayoutDetailBottomBinding bottomBinding;
    private LayoutDetailSelectImageBinding selectImageBinding;
    private DetailPresenter presenter;
    private ConversationModel model;
    private List<MessageModel> lstMessage = new ArrayList<>();
    private MessageGroupAdapter messageAdapter;
    private String pathImageSelect;
    private HomeViewModel homeViewModel;

    @Override
    protected void stateNetWork(boolean isAvaiable) {

    }

    private HomeViewModel getViewModel() {
        return homeViewModel;
    }

    @Override
    protected View getLayoutResource() {
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        topBinding = binding.layoutTop;
        bottomBinding = binding.layoutBottom;
        selectImageBinding = binding.layoutSelectImage;
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        presenter = new DetailPresenter(this);
        topBinding.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvBlock.setText(Html.fromHtml(getString(R.string.block_content), Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.tvBlock.setText(Html.fromHtml(getString(R.string.block_content)));
        }
    }


    @Override
    protected void initData() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        model = (ConversationModel) getIntent().getSerializableExtra(DATA_SELECT);
        if (model == null)
            return;
        lstMessage.clear();
        lstMessage = model.getMessageModels();
        loadViewStatus();
    }

    private void loadViewStatus() {
        setColorConversation();
        setNameConversation();
        setColorConversation();
        setImageConversation();
        setDataScrollView();
        if (model.isBlock())
            setStatusBlock(true);
    }

    @Override
    public void setDataScrollView() {
        /*add header*/
        if (lstMessage.isEmpty() || lstMessage.get(0).getType() != Config.TYPE_HEAEDER) {
            MessageModel messageModel = new MessageModel();
            messageModel.setType(Config.TYPE_HEAEDER);
            lstMessage.add(0, messageModel);
        }
        messageAdapter = new MessageGroupAdapter(this, model, lstMessage);
        binding.rcvMessage.setAdapter(messageAdapter);
        messageAdapter.setItemClickListener((position, itemType, view) -> {
            if (itemType == Config.TYPE_HEAEDER) {
                showDialogEditInfor();
            } else {
                showPopupOptItemMessager(position, itemType, view);
            }
        });
        scrollLastItemMessage();
    }

    @Override
    public void showDialogEditInfor() {
//        new DialogEditProfile.ExtendBuilder()
//                .setFriendOn(model.getFriendOn())
//                .setLiveIn(model.getLiveIn())
//                .setTitle(getString(R.string.edit_profile))
//                .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
//                    model.setFriendOn((String) data.get(Config.KEY_FRIEND_ON));
//                    model.setLiveIn((String) data.get(Config.KEY_LIVE_IN));
//                    if (messageAdapter != null)
//                        messageAdapter.notifyItemChanged(0);
//                    if (presenter != null)
//                        presenter.updateConvesation(model);
//                    baseDialog.dismiss();
//                })
//                .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
//                    baseDialog.dismiss();
//                })
//                .build()
//                .show(getSupportFragmentManager(), DialogEditProfile.class.getName());
    }

    @Override
    public void showPopupOptItemMessager(int position, int type, View view) {
        MessageModel messageModel = lstMessage.get(position);
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater()
                .inflate(R.menu.menu_item_message, popup.getMenu());
        if (type == Config.TYPE_RECORD || type == Config.TYPE_STICKER || type == Config.TYPE_IMAGE
                || type == Config.TYPE_REMOVE)
            popup.getMenu().findItem(R.id.menu_edit).setVisible(false);
        if (type == Config.TYPE_REMOVE || type == Config.TYPE_DATETIME)
            popup.getMenu().findItem(R.id.menu_remove_message).setVisible(false);
        if (model.isGroup() || type == Config.TYPE_DATETIME) {
            popup.getMenu().findItem(R.id.menu_remove_message).setVisible(false);
            popup.getMenu().findItem(R.id.menu_set_seen).setVisible(false);
            popup.getMenu().findItem(R.id.menu_set_received).setVisible(false);
            popup.getMenu().findItem(R.id.menu_not_send).setVisible(false);
            popup.getMenu().findItem(R.id.menu_not_received).setVisible(false);
        }
        popup.setOnMenuItemClickListener(item -> {
            if (presenter == null)
                return true;
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    lstMessage.remove(position);
                    if (presenter != null)
                        presenter.updateConvesation(model);
                    if (messageAdapter != null)
                        messageAdapter.notifyDataSetChanged();
                    break;
                case R.id.menu_edit:
                    new DialogEditMessage.ExtendBuilder()
                            .setMessageContent(messageModel.getChatContent())
                            .setTitle(getString(R.string.edit))
                            .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                                messageModel.setChatContent((String) data.get(Config.KEY_CONTENT));
                                if (presenter != null)
                                    presenter.updateConvesation(model);
                                if (messageAdapter != null)
                                    messageAdapter.notifyDataSetChanged();
                                baseDialog.dismiss();
                            })
                            .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                                baseDialog.dismiss();
                            })
                            .build()
                            .show(getSupportFragmentManager(), DialogEditMessage.class.getName());
                    break;
                case R.id.menu_set_seen:
                    messageModel.setStatus(Config.STATUS_SEEN);
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(position);
                    break;
                case R.id.menu_set_received:
                    messageModel.setStatus(Config.STATUS_RECEIVED);
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(position);
                    break;
                case R.id.menu_not_send:
                    messageModel.setStatus(Config.STATUS_NOT_SEND);
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(position);
                    break;
                case R.id.menu_not_received:
                    messageModel.setStatus(Config.STATUS_NOT_RECEIVED);
                    if (messageAdapter != null)
                        messageAdapter.notifyItemChanged(position);
                    break;
                case R.id.menu_remove_message:
                    messageModel.setType(Config.TYPE_REMOVE);
                    if (messageAdapter != null)
                        messageAdapter.notifyDataSetChanged();
                    break;
            }
            return true;
        });
        popup.show();
    }

    @Override
    protected void initEvent() {
        topBinding.imBack.setOnClickListener(this);
        topBinding.imCall.setOnClickListener(this);
        topBinding.imVideo.setOnClickListener(this);
        topBinding.imInfor.setOnClickListener(this);
//        topBinding.imOutline.setOnClickListener(this);
        bottomBinding.edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setStatusItemBottom(count != 0, false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bottomBinding.imMore.setOnClickListener(this);
        bottomBinding.imEmoji.setOnClickListener(this);
        bottomBinding.imSend.setOnClickListener(this);
        bottomBinding.imPicture.setOnClickListener(this);
        bottomBinding.imRecord.setOnClickListener(this);
        bottomBinding.imTakePhoto.setOnClickListener(this);

        selectImageBinding.imRemoveImage.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.updateConvesation(model);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_more:
                if (!bottomBinding.llOpt.isShown()) {
                    setStatusItemBottom(false, true);
                }
                break;
            case R.id.im_back:
                onBackPressed();
                break;
            case R.id.im_call:
//                Intent intent = new Intent(this, CallActivity.class);
//                intent.putExtra(ConversationDetailActivity.DATA_SELECT, model);
//                startActivity(intent);
                break;
            case R.id.im_video:
//                startActivity(new Intent(this, VideoActivity.class));
                break;
            case R.id.im_infor:
            case R.id.im_outline:
                showPopupInfor(v);
                break;
            case R.id.im_send:
                showPopupOptSend();
                break;
            case R.id.im_record:
                new DialogCreateRecord.ExtendBuilder()
                        .setTitle(getString(R.string.voice_message))
                        .onSetPositiveButton(getString(R.string.ok), (baseDialog, data) -> {
                            Boolean isSend = (Boolean) data.get(Config.KEY_TYPE_SEND);
                            String time = (String) data.get(Config.KEY_CONTENT);
                            validateMemberSend(isSend, userMessageModel -> {
                                MessageModel message = Config.createMessageModel(time, Config.TYPE_RECORD);
                                message.setUserOwn(userMessageModel != null
                                        ? userMessageModel.getId()
                                        : model.getId());
                                message.setSend(isSend);
                                lstMessage.add(message);
                                if (presenter != null) {
                                    presenter.updateConvesation(model);
                                }
                                scrollLastItemMessage();
                            });
                            baseDialog.dismiss();
                        })
                        .onSetNegativeButton(getString(R.string.cancel), baseDialog -> {
                            baseDialog.dismiss();
                        })
                        .build()
                        .show(this.getSupportFragmentManager(), DialogEditMessage.class.getName());
                break;
            case R.id.im_emoji:
                Intent intentEmoji = new Intent(this, StickerActivitySticker.class);
                startActivityForResult(intentEmoji, Config.REQUEST_CODE_ACT_STICKER);
                break;
            case R.id.im_picture:
                try {
                    Utils.askPermissionStorage(this, () -> {
                        Utils.requestGetGallery(this);
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.im_remove_image:
                cleanImageAfterSend();
                break;
            case R.id.im_take_photo:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.ask_save_image))
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            saveImage();
                            dialog.cancel();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {
                            dialog.cancel();
                        })
                        .show();
                break;
        }
    }

    @Override
    public void saveImage() {
        try {
            Utils.askPermissionStorage(this, () -> {
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

    @Override
    public void showPopupInfor(View view) {
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
                        getViewModel().deleteMessByOwner(getBaseContext(), (int) model.getId(), v -> {
                            if (getViewModel().objectMessenges.isEmpty() || getViewModel().objectMessenges.get(0).getType() != Config.TYPE_HEAEDER) {
                                ObjectMessenge messageModel = new ObjectMessenge();
                                messageModel.setType(Config.TYPE_HEAEDER);
                                getViewModel().objectMessenges.add(0, messageModel);
                            }
                            messageAdapter.notifyDataSetChanged();
                        });
                    });
                    break;
            }
            return true;
        });
        popup.setForceShowIcon(true);
        popup.show();
    }

    @Override
    public void setStatusBlock(boolean isBlock) {
        if (isBlock) {
            binding.tvBlock.setVisibility(View.VISIBLE);
            bottomBinding.container.setVisibility(View.GONE);
            topBinding.imCall.setVisibility(View.GONE);
            topBinding.imVideo.setVisibility(View.GONE);
            topBinding.tvContent.setVisibility(View.GONE);
            topBinding.imStatus.setVisibility(View.GONE);
            if (!model.isGroup()) {
//                ImageUtils.loadImage(topBinding.imOutline, model.getImage());
                topBinding.imInline.setVisibility(View.GONE);
//                topBinding.imOutline.clearColorFilter();
            }
        } else {
            binding.tvBlock.setVisibility(View.GONE);
            bottomBinding.container.setVisibility(View.VISIBLE);
            topBinding.imCall.setVisibility(View.VISIBLE);
            topBinding.imVideo.setVisibility(View.VISIBLE);
            topBinding.imStatus.setVisibility(View.VISIBLE);
            if (!model.isGroup()) {
                topBinding.tvContent.setVisibility(View.VISIBLE);
                topBinding.imInline.setVisibility(View.VISIBLE);
//                topBinding.imOutline.setImageResource(R.drawable.bg_round_0084f0);
//                topBinding.imOutline.setColorFilter(Color.parseColor(model.getColor()), PorterDuff.Mode.SRC_IN);
                ImageUtils.loadImage(getApplicationContext(), topBinding.imInline, model.getImage());
            }
        }
    }

    @Override
    public void setImageConversation() {
        if (model.isGroup()) {
//            topBinding.imOutline.setVisibility(View.GONE);
            topBinding.imStatus.setVisibility(model.isActive() ? View.VISIBLE : View.GONE);
            topBinding.imInline.setVisibility(View.GONE);
            topBinding.llAvatarGroup.setVisibility(View.VISIBLE);
            topBinding.tvContent.setVisibility(View.GONE);
            List<DaoContact> lstMember = model.getUserMessageModels();
            if (!TextUtils.isEmpty(model.getImage()) && !"null".equals(model.getImage())) {
                ImageUtils.loadImage(getApplicationContext(), topBinding.imAvaGroup1, model.getImage());
                if (lstMember.size() > 0) {
                    ImageUtils.loadImage(getApplicationContext(), topBinding.imAvaGroup2, lstMember.get(0).getAvatar());
                }
            } else {
                if (lstMember.size() > 0) {
                    ImageUtils.loadImage(getApplicationContext(), topBinding.imAvaGroup1, lstMember.get(0).getAvatar());
                }
                if (lstMember.size() > 1) {
                    ImageUtils.loadImage(getApplicationContext(), topBinding.imAvaGroup2, lstMember.get(1).getAvatar());
                }
            }

        } else {
//            topBinding.imOutline.setVisibility(View.VISIBLE);
            topBinding.llAvatarGroup.setVisibility(View.GONE);
            if (!model.isActive()) {
                topBinding.imStatus.setVisibility(View.GONE);
                ImageUtils.loadImage(getApplicationContext(), topBinding.imOutline, model.getImage());
                topBinding.imInline.setVisibility(View.GONE);
//                topBinding.imOutline.clearColorFilter();
                if (!TextUtils.isEmpty(model.getTimeActiveAgo())) {
                    topBinding.tvContent.setText(model.getTimeActiveAgo());
                } else {
                    topBinding.tvContent.setVisibility(View.GONE);
                }
            } else {
                topBinding.imStatus.setVisibility(View.VISIBLE);
                topBinding.imInline.setVisibility(View.VISIBLE);
//                topBinding.imOutline.setImageResource(R.drawable.bg_round_0084f0);
//                topBinding.imOutline.setColorFilter(Color.parseColor(model.getColor()), PorterDuff.Mode.SRC_IN);
                ImageUtils.loadImage(getApplicationContext(), topBinding.imInline, model.getImage());
            }
        }
    }

    @Override
    public void setNameConversation() {
        if (!TextUtils.isEmpty(model.getName()))
            topBinding.tvName.setText(model.getName());
    }

    @Override
    public void setStatusItemBottom(boolean isChating, boolean foreShow) {
        if (isChating) {
            bottomBinding.llOpt.setVisibility(View.GONE);
            bottomBinding.imSend.setImageResource(R.drawable.ic_send);
            bottomBinding.imMore.setImageResource(R.drawable.ic_next);
        } else {
            bottomBinding.llOpt.setVisibility(View.VISIBLE);
            bottomBinding.imMore.setImageResource(R.drawable.ic_more);
            if (foreShow) {
                if (TextUtils.isEmpty(bottomBinding.edtInput.getText().toString())) {
                    bottomBinding.imSend.setImageResource(R.drawable.ic_like);
                } else {
                    bottomBinding.imSend.setImageResource(R.drawable.ic_send);
                }
            } else {
                bottomBinding.imSend.setImageResource(R.drawable.ic_like);
            }
        }

        if (selectImageBinding.container.isShown()) {
            bottomBinding.imSend.setImageResource(R.drawable.ic_send);
        }
    }

    @Override
    public void setColorConversation() {
        int color = Color.parseColor(model.getColor());
        binding.tvBlock.setBackgroundColor(color);
        /**/
        topBinding.imBack.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        topBinding.imCall.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        topBinding.imVideo.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        topBinding.imInfor.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        /**/
        bottomBinding.imSend.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imMore.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imPicture.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imRecord.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imTakePhoto.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        bottomBinding.imEmoji.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void showPopupOptSend() {
        PopupMenu popup = new PopupMenu(this, bottomBinding.imSend);
        popup.getMenuInflater()
                .inflate(R.menu.menu_send_message, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            boolean isSend = item.getItemId() == R.id.menu_send;
            validateMemberSend(isSend, userMessageModel -> {
                String data;
                int type;
                if (selectImageBinding.container.isShown()) {
                    data = pathImageSelect;
                    type = Config.TYPE_IMAGE;
                    cleanImageAfterSend();
                } else if (!TextUtils.isEmpty(bottomBinding.edtInput.getText().toString())) {
                    data = bottomBinding.edtInput.getText().toString();
                    type = Config.TYPE_TEXT;
                    cleanChatAfterSend();
                } else {
                    data = Config.IC_LIKE_NAME;
                    type = Config.TYPE_STICKER;
                }
                MessageModel message = Config.createMessageModel(data, type);
                message.setSend(isSend);
                message.setUserOwn(userMessageModel != null
                        ? userMessageModel.getId()
                        : model.getId());
                lstMessage.add(message);
                if (presenter != null) {
                    presenter.updateConvesation(model);
                }
                scrollLastItemMessage();
            });
            return true;
        });
        popup.show();
    }

    @Override
    public void validateMemberSend(boolean isSend, IActionDetail.
            MemberSelectLisnter memberSelectLisnter) {
        if (!model.isGroup() || isSend) {
            if (memberSelectLisnter != null)
                memberSelectLisnter.onMemberSelectListener(null);
        } else {
            List<DaoContact> lstMember = model.getUserMessageModels();
            if (lstMember.isEmpty()) {
                toast(getString(R.string.no_user_found));
                return;
            }
            new DialogSelectMember.ExtendBuilder()
                    .setLstMember(model.getUserMessageModels())
                    .setItemSelectListener(userMessageModel -> {
                        if (memberSelectLisnter != null)
                            memberSelectLisnter.onMemberSelectListener(userMessageModel);
                    })
                    .build()
                    .show(getSupportFragmentManager(), DialogSelectMember.class.getName());
        }
    }


    @Override
    public void scrollLastItemMessage() {
        if (messageAdapter != null)
            messageAdapter.notifyDataSetChanged();
        binding.rcvMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
        bottomBinding.edtInput.setFocusableInTouchMode(true);
        bottomBinding.edtInput.requestFocus(0);
    }

    @Override
    public void cleanChatAfterSend() {
        bottomBinding.edtInput.setText("");
    }

    @Override
    public void cleanImageAfterSend() {
        selectImageBinding.container.setVisibility(View.GONE);
        pathImageSelect = "";
        if (!TextUtils.isEmpty(bottomBinding.edtInput.getText().toString())) {
            bottomBinding.imSend.setImageResource(R.drawable.ic_send);
        } else {
            bottomBinding.imSend.setImageResource(R.drawable.ic_like);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == Config.REQUEST_CODE_ACT_SETTING) {
            model = (ConversationModel) data.getSerializableExtra(Config.RESULRT_DATA);
            loadViewStatus();
        } else if (requestCode == Config.REQUEST_CODE_ACT_DATETIME) {
            String dateTime = data.getStringExtra(Config.RESULRT_DATA);
            lstMessage.add(Config.createMessageModel(dateTime, Config.TYPE_DATETIME));
            scrollLastItemMessage();
        } else if (requestCode == Config.REQUEST_CODE_ACT_STICKER) {
            String resName = data.getStringExtra(Config.KEY_CONTENT);
            boolean isSend = data.getBooleanExtra(Config.KEY_TYPE_SEND, true);
            validateMemberSend(isSend, userMessageModel -> {
                MessageModel messageModel = Config.createMessageModel(resName, Config.TYPE_STICKER);
                messageModel.setSend(isSend);
                messageModel.setUserOwn(userMessageModel != null
                        ? userMessageModel.getId()
                        : model.getId());
                lstMessage.add(messageModel);
                scrollLastItemMessage();
            });
        } else if (requestCode == Config.REQUEST_CODE_ACT_MEMBER) {
            model = (ConversationModel) data.getSerializableExtra(Config.RESULRT_DATA);
            setImageConversation();
        } else if (requestCode == Const.PICK_FROM_GALLERY && data != null && data.getData() != null) {
            CropImage.activity(data.getData())
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == -1 && null != data) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            pathImageSelect = resultUri.toString();
            binding.layoutSelectImage.container.setVisibility(View.VISIBLE);
            Glide.with(this).load(resultUri).into(binding.layoutSelectImage.imPreview);
            binding.layoutBottom.imSend.setImageResource(R.drawable.ic_send);
        }

        if (presenter != null) {
            presenter.updateConvesation(model);
        }
    }
}
