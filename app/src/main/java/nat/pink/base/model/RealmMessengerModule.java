package nat.pink.base.model;

import io.realm.annotations.RealmModule;

@RealmModule(library = true, classes = {ConversationObject.class, MessageObject.class, UserMessageObject.class})
public class RealmMessengerModule {
}
