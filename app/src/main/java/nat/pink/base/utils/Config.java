package nat.pink.base.utils;


import android.content.res.Resources;

import nat.pink.base.base.App;
import nat.pink.base.model.ConversationModel;
import nat.pink.base.model.ObjectMessenge;

public class Config {
    public static final String KEY_CONTENT = "message content";
    public static final String KEY_TYPE_SEND = "type send";
    public static final String IC_LIKE_NAME = "sticker_like";

    public static final int REQUEST_CODE_ACT_STICKER = 1259;
    public static final int CHECK_TURN_OFF_VOICE = 1260;
    public static final int CHECK_TURN_OFF_VOICE_INCOMING = 1261;

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

    public static ObjectMessenge createMessage(String data, int type) {
        ObjectMessenge messageModel = new ObjectMessenge();
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
}
