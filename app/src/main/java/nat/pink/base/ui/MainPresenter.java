package nat.pink.base.ui;

import java.util.HashMap;
import java.util.List;

import nat.pink.base.base.repository.AppDataRepository;
import nat.pink.base.model.ConversationModel;
import nat.pink.base.model.ConversationObject;
import nat.pink.base.ui.home.HomeFragment;
import nat.pink.base.utils.Config;

public class MainPresenter implements IActionMain.IPresenter {

    private HomeFragment view;
    private AppDataRepository repository;

    public MainPresenter(HomeFragment view) {
        this.view = view;
        repository = AppDataRepository.getRepository();
    }

    @Override
    public void createConvesation(HashMap<String, Object> data) {
        ConversationModel model = Config.setConverstationDefault();
        model.setName(String.valueOf(data.get(Config.KEY_TITLE)));
        model.setImage(String.valueOf(data.get(Config.KEY_AVATAR)));
        model.setGroup((Boolean) data.get(Config.KEY_GROUP));
        repository.saveConversation(model);
        getListConversation();
    }

    @Override
    public void getListConversation() {
        List<ConversationModel> lstDataHorizontal = repository.getListDataHorizontal();
        List<ConversationModel> lstDataVertical = repository.getListDataVertical();

        getCountReceived();
    }

    @Override
    public void deleteConvesation(ConversationModel conversationModel) {
        repository.deleteConversation(conversationModel);
        getListConversation();
    }

    @Override
    public void setStatusConversation(ConversationModel conversation, int status) {
        conversation.setStatus(status);
        repository.update(new ConversationObject(conversation));
    }

    @Override
    public void getCountReceived() {
        int countReceived = repository.getCountReceived();
    }
}
