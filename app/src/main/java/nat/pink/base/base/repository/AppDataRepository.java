package nat.pink.base.base.repository;


import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.Sort;
import nat.pink.base.base.DatabaseManager;
import nat.pink.base.base.RealmDatabaseManager;
import nat.pink.base.base.RealmProvider;
import nat.pink.base.model.ConversationModel;
import nat.pink.base.model.ConversationObject;

public class AppDataRepository extends BaseRepository<ConversationObject> {

    private static AppDataRepository repository;

    public static AppDataRepository getRepository() {
        if (repository == null) {
            DatabaseManager<ConversationObject> databaseManager = new RealmDatabaseManager<>(RealmProvider.realmMessenger);
            repository = new AppDataRepository(databaseManager);
        }
        return repository;
    }

    public AppDataRepository(DatabaseManager<ConversationObject> databaseManager) {
        super(databaseManager);
    }

    public List<ConversationModel> getListDataHorizontal() {
        List<ConversationModel> conversationModels = new ArrayList<>();
        List<ConversationObject> conversationObjects = fetch(realmQuery(ConversationObject.class)
                .equalTo("isGroup", false)
                .sort(new String[]{"isActive", "lastTimeChat"}, new Sort[]{Sort.DESCENDING, Sort.DESCENDING}));
        for (ConversationObject object : conversationObjects) {
            conversationModels.add(new ConversationModel(object));
        }
        return conversationModels;
    }

    public List<ConversationModel> getListDataVertical() {
        List<ConversationModel> conversationModels = new ArrayList<>();
        List<ConversationObject> conversationObjects = fetch(realmQuery(ConversationObject.class)
                .sort(new String[]{"lastTimeChat"}, new Sort[]{Sort.DESCENDING}));
        for (ConversationObject object : conversationObjects) {
            conversationModels.add(new ConversationModel(object));
        }
        return conversationModels;
    }

    public int getCountReceived() {
        List<ConversationObject> conversationObjects = fetch(realmQuery(ConversationObject.class)
                .equalTo("status", 2));
        return conversationObjects.size();
    }

    public void saveConversation(ConversationModel ConversationObject) {
        update(new ConversationObject(ConversationObject));
    }

    public void saveAllConversation(List<ConversationModel> conversationModels) {
        for (ConversationModel conversationModel :
                conversationModels) {
            update(new ConversationObject(conversationModel));
        }
    }

    public void deleteConversation(ConversationModel conversationModel) {
        RealmQuery<ConversationObject> query = realmQuery(ConversationObject.class)
                .equalTo("id", conversationModel.getId());
        delete(fetch(query).get(0));
    }

}