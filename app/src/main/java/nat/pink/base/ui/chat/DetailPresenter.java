package nat.pink.base.ui.chat;

import nat.pink.base.base.repository.AppDataRepository;
import nat.pink.base.model.ConversationModel;
import nat.pink.base.model.ConversationObject;
import nat.pink.base.utils.Config;

public class DetailPresenter implements IActionDetail.IPresenter {

    private ConversationDetailActivity view;
    private AppDataRepository repository;

    public DetailPresenter(ConversationDetailActivity view) {
        this.view = view;
        repository = AppDataRepository.getRepository();
    }

    @Override
    public void updateConvesation(ConversationModel model) {
        ConversationObject modelObject = new ConversationObject(model);

        if (!modelObject.getMessageObjects().isEmpty() && modelObject.getMessageObjects().get(0).getType() == Config.TYPE_HEAEDER)
            modelObject.getMessageObjects().remove(0);
        repository.update(modelObject);
    }
}
