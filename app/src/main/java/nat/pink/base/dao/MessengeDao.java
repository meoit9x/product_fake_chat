package nat.pink.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import nat.pink.base.model.DaoContact;
import nat.pink.base.model.ObjectMessenge;

@Dao
public interface MessengeDao {

    @Insert
    Long insertMessenge(ObjectMessenge objectMessenge);

    @Query("SELECT * FROM message WHERE userOwn IN (:userOwn)")
    List<ObjectMessenge> getMessageByOwnId(int userOwn);

    @Query("DELETE FROM message WHERE id IN (:id)")
    void deleteMessage(int id);

    @Query("DELETE FROM message WHERE userOwn IN (:id)")
    void deleteMessageByOwn(int id);
}
