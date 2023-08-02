package nat.pink.base.ui;

import android.view.View;


import java.util.HashMap;
import java.util.List;

import nat.pink.base.model.ConversationModel;

public interface IActionMain {
    interface IView {
        void showPopupSelectCreateChat();

        void showPopupItemConversation(int index, View item);

        void setImageAvatar(String url);

        void showDialogCreateChat(boolean isCreateGroup);

        void fillListDataHorizontal(List<ConversationModel> lstData);

        void fillListDataVertical(List<ConversationModel> lstData);

        void deleteConversationSuccess();

        void setCountReceiverd(int count);

    }

    interface IPresenter {
        void createConvesation(HashMap<String, Object> data);

        void getListConversation();

        void deleteConvesation(ConversationModel conversationModel);

        void setStatusConversation(ConversationModel conversation, int status);

        void getCountReceived();
    }
}
