package nat.pink.base.utils;


import android.content.res.Resources;

import java.util.List;

import nat.pink.base.R;
import nat.pink.base.base.App;
import nat.pink.base.model.ConversationModel;
import nat.pink.base.model.DaoContact;
import nat.pink.base.model.MessageModel;
import nat.pink.base.model.MessageObject;
import nat.pink.base.model.ObjectMessenge;
import nat.pink.base.model.UserMessageModel;

public class Config {
    public static final String KEY_CONTENT = "message content";
    public static final String KEY_TYPE_SEND = "type send";
    public static final String IC_LIKE_NAME = "sticker_like";
    public static final String RESULRT_DATA = "result data";
    public static final int REQUEST_CODE_ACT_STICKER = 1259;
    public static final int CHECK_TURN_OFF_VOICE = 1260;
    public static final int CHECK_TURN_OFF_VOICE_INCOMING = 1261;
    public static final int REQUEST_CODE_ACT = 1257;
    public static final int REQUEST_CODE_ACT_DATETIME = 1258;
    public static final int REQUEST_CODE_ACT_SETTING = 1260;
    public static final int REQUEST_CODE_ACT_MEMBER = 1261;
    public static final int TYPE_HEAEDER = -1;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_DATETIME = 2;
    public static final int TYPE_RECORD = 3;
    public static final int TYPE_STICKER = 4;
    public static final int TYPE_IMAGE = 5;
    public static final int TYPE_REMOVE = 6;

    public static final int STATUS_SEEN = 1;
    public static final int STATUS_RECEIVED = 2;
    public static final int STATUS_NOT_SEND = 3;
    public static final int STATUS_NOT_RECEIVED = 4;

    public static final String KEY_TITLE = "title conversation";
    public static final String KEY_AVATAR = "avatar conversation";
    public static final String KEY_GROUP = "conversation is group";
    public static final String KEY_STATUS_ON = "status on";
    public static final String KEY_LIST_USER = "list user";
    public static final String KEY_NAME_GROUP = "name group";

    public static ObjectMessenge createMessage(String data, int type) {
        ObjectMessenge messageModel = new ObjectMessenge();
        messageModel.setChatContent(data);
        messageModel.setDateTime(System.currentTimeMillis());
        messageModel.setType(type);
        return messageModel;
    }

    public static MessageModel createMessageModel(String data, int type) {
        MessageModel messageModel = new MessageModel();
        messageModel.setChatContent(data);
        messageModel.setDateTime(System.currentTimeMillis());
        messageModel.setType(type);
        return messageModel;
    }

    public static ConversationModel setConverstationDefault() {
        Resources resources = App.getInstance().getResources();
        ConversationModel conversationModel = new ConversationModel();
        conversationModel.setId(System.currentTimeMillis());
        conversationModel.setLastTimeChat(System.currentTimeMillis());
        conversationModel.setActive(true);
        conversationModel.setStatus(STATUS_SEEN);
        conversationModel.setColor("#0084f0");
        return conversationModel;
    }

    public static DaoContact getMemberSendMessage(long id, List<DaoContact> lstMember) {
        DaoContact userMessageModel = new DaoContact();
        userMessageModel.setId((int) System.currentTimeMillis());
        if (lstMember.isEmpty())
            return userMessageModel;
        for (DaoContact model : lstMember)
            if (model.getId() == id)
                return model;
        return userMessageModel;
    }

    public static int getBackgroundChatResoure(ConversationModel conversationModel, int currentIndex) {
        List<MessageModel> listData = conversationModel.getMessageModels();
        boolean isSend = listData.get(currentIndex).isSend();
        MessageModel messageCurrent = listData.get(currentIndex);
        if (isSend) {
            if (currentIndex == listData.size() - 1) {
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (messagePrevious.isSend()) {
                    if (messagePrevious.getType() != Config.TYPE_TEXT)
                        return R.drawable.bg_chat_single;
                    else
                        return R.drawable.bg_chat_right_bottom;
                } else {
                    return R.drawable.bg_chat_single;
                }
            } else {
                MessageModel messageNext = listData.get(currentIndex + 1);
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (!messageNext.isSend()) {
                    if (!messagePrevious.isSend() || messagePrevious.getType() != Config.TYPE_TEXT)
                        return R.drawable.bg_chat_single;
                    else
                        return R.drawable.bg_chat_right_bottom;
                } else if (!messagePrevious.isSend()) {
                    if (!messageNext.isSend() || messageNext.getType() != Config.TYPE_TEXT)
                        return R.drawable.bg_chat_single;
                    else
                        return R.drawable.bg_chat_right_top;
                } else {
                    if (messagePrevious.getType() != Config.TYPE_TEXT) {
                        if (messageNext.getType() != Config.TYPE_TEXT)
                            return R.drawable.bg_chat_single;
                        else
                            return R.drawable.bg_chat_right_top;
                    } else if (messageNext.getType() != Config.TYPE_TEXT) {
                        if (messagePrevious.getType() != Config.TYPE_TEXT)
                            return R.drawable.bg_chat_single;
                        else
                            return R.drawable.bg_chat_right_bottom;
                    } else {
                        return R.drawable.bg_chat_right_center;
                    }
                }
            }
        } else {
            MessageModel messagePrevious = listData.get(currentIndex - 1);
            DaoContact userPrevious = getMemberSendMessage(messagePrevious.getUserOwn()
                    , conversationModel.getUserMessageModels());
            DaoContact userCurrent = getMemberSendMessage(messageCurrent.getUserOwn()
                    , conversationModel.getUserMessageModels());
            if (listData.size() == 2) {
                return R.drawable.bg_chat_single;
            } else if (currentIndex == listData.size() - 1) {
                if (messagePrevious.isSend()) {
                    return R.drawable.bg_chat_single;
                } else {
                    if (userPrevious.getId() == userCurrent.getId()) {
                        if (messagePrevious.getType() == Config.TYPE_TEXT) {
                            return R.drawable.bg_chat_left_bottom;
                        } else {
                            return R.drawable.bg_chat_single;
                        }
                    } else {
                        return R.drawable.bg_chat_single;
                    }
                }
            } else {
                MessageModel messageNext = listData.get(currentIndex + 1);
                DaoContact userNext = getMemberSendMessage(messageNext.getUserOwn()
                        , conversationModel.getUserMessageModels());
                if (messageNext.isSend() && messagePrevious.isSend()) {
                    /*tin nhắn trước và sau đều là tin gửi*/
                    return R.drawable.bg_chat_single;
                } else if (!messagePrevious.isSend() && messageNext.isSend()) {
                    /*tin nhắn sau là tin nhận*/
                    if (userPrevious.getId() == userCurrent.getId()) {
                        /*tin sau với tin hiện tại cùng người gửi*/
                        if (messageCurrent.getType() == Config.TYPE_TEXT) {
                            /*tin hiện tại có phải là tin văn bản*/
                            return R.drawable.bg_chat_left_bottom;
                        } else {
                            return R.drawable.bg_chat_single;
                        }
                    } else {
                        return R.drawable.bg_chat_single;
                    }
                } else if (!messageNext.isSend() && messagePrevious.isSend()) {
                    /*Tin nhắn trước là tin nhận*/
                    if (userNext.getId() == userCurrent.getId()) {
                        /*tin trước với tin hiện tại cùng người gửi*/
                        if (messageCurrent.getType() == Config.TYPE_TEXT) {
                            /*tin hiện tại có phải là tin văn bản*/
                            return R.drawable.bg_chat_left_top;
                        } else {
                            return R.drawable.bg_chat_single;
                        }
                    } else {
                        return R.drawable.bg_chat_single;
                    }
                } else {
                    /*tin trước và tin sau đều là tin nhận*/
                    if (userCurrent.getId() == userPrevious.getId()
                            && userCurrent.getId() == userNext.getId()) {
                        /*Cùng 1 người gửi*/
                        if (messageNext.getType() == Config.TYPE_TEXT
                                && messagePrevious.getType() == Config.TYPE_TEXT) {
                            return R.drawable.bg_chat_left_center;
                        } else if (messageNext.getType() == Config.TYPE_TEXT) {
                            return R.drawable.bg_chat_left_top;
                        } else if (messagePrevious.getType() == Config.TYPE_TEXT) {
                            return R.drawable.bg_chat_left_bottom;
                        } else {
                            return R.drawable.bg_chat_single;
                        }
                    } else if (userCurrent.getId() != userPrevious.getId()
                            && userCurrent.getId() == userNext.getId()) {
                        /*Tin sau với tin hiện tại ko cùng người gửi*/
                        if (messageNext.getType() != Config.TYPE_TEXT) {
                            /*Tin truoc ko phải tin văn bản*/
                            return R.drawable.bg_chat_single;
                        } else {
                            return R.drawable.bg_chat_left_top;
                        }
                    } else if (userCurrent.getId() != userNext.getId()
                            && userCurrent.getId() == userPrevious.getId()) {
                        /*Tin trước với tin hiện tại ko cùng người gửi*/
                        if (messagePrevious.getType() != Config.TYPE_TEXT) {
                            /*Tin sau ko phải tin văn bản*/
                            return R.drawable.bg_chat_single;
                        } else {
                            return R.drawable.bg_chat_left_bottom;
                        }
                    } else {
                        return R.drawable.bg_chat_single;
                    }
                }
            }
        }
    }

    public static int getBackgroundRemoveResoure(List<MessageModel> listData, int currentIndex) {
        MessageModel messageCurrent = listData.get(currentIndex);
        if (messageCurrent.isSend()) {
            if (currentIndex == listData.size() - 1) {
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (messagePrevious.isSend()) {
                    if (messagePrevious.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_right_bottom;
                } else {
                    return R.drawable.bg_remove_single;
                }
            } else {
                MessageModel messageNext = listData.get(currentIndex + 1);
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (!messageNext.isSend()) {
                    if (!messagePrevious.isSend() || messagePrevious.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_right_bottom;
                } else if (!messagePrevious.isSend()) {
                    if (!messageNext.isSend() || messageNext.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_right_top;
                } else {
                    if (messagePrevious.getType() != Config.TYPE_REMOVE) {
                        if (messageNext.getType() != Config.TYPE_REMOVE)
                            return R.drawable.bg_remove_single;
                        else
                            return R.drawable.bg_remove_right_top;
                    } else if (messageNext.getType() != Config.TYPE_REMOVE) {
                        if (messagePrevious.getType() != Config.TYPE_REMOVE)
                            return R.drawable.bg_remove_single;
                        else
                            return R.drawable.bg_remove_right_bottom;
                    } else {
                        return R.drawable.bg_remove_right_center;
                    }
                }
            }
        } else {
            if (currentIndex == listData.size() - 1) {
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (!messagePrevious.isSend()) {
                    if (messagePrevious.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_left_bottom;
                } else {
                    return R.drawable.bg_remove_single;
                }
            } else {
                MessageModel messageNext = listData.get(currentIndex + 1);
                MessageModel messagePrevious = listData.get(currentIndex - 1);
                if (messageNext.isSend()) {
                    if (messagePrevious.isSend() || messagePrevious.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_left_bottom;
                } else if (messagePrevious.isSend()) {
                    if (messageNext.isSend() || messageNext.getType() != Config.TYPE_REMOVE)
                        return R.drawable.bg_remove_single;
                    else
                        return R.drawable.bg_remove_left_top;
                } else {
                    if (messagePrevious.getType() != Config.TYPE_REMOVE) {
                        if (messageNext.getType() != Config.TYPE_REMOVE)
                            return R.drawable.bg_remove_single;
                        else
                            return R.drawable.bg_remove_left_top;
                    } else if (messageNext.getType() != Config.TYPE_REMOVE) {
                        if (messagePrevious.getType() != Config.TYPE_REMOVE)
                            return R.drawable.bg_remove_single;
                        else
                            return R.drawable.bg_remove_left_bottom;
                    } else {
                        return R.drawable.bg_remove_left_center;
                    }
                }
            }
        }
    }
}
