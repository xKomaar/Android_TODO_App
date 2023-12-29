package pl.sm_projekt_aplikacjatodo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProfileDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profile profile);

    @Delete
    void delete(Profile profile);

    @Update
    void update(Profile profile);

    @Query("DELETE FROM Profile")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM Profile")
    public List<ProfileWithTasks> findAllProfilesWithTasks();

    @Transaction
    @Query("SELECT * FROM Profile WHERE profileId = :profileId")
    public ProfileWithTasks findProfileWithTasksByProfileId(int profileId);
}