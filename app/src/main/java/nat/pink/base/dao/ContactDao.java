package nat.pink.base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import nat.pink.base.model.DaoContact;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact")
    List<DaoContact> getAllContact();

    @Insert
    Long insertContact(DaoContact daoContact);

    @Update
    int updateContact(DaoContact daoContact);

    @Delete
    void deleteContact(DaoContact daoContact);

}
