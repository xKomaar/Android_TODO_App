package pl.sm_projekt_aplikacjatodo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import pl.sm_projekt_aplikacjatodo.Profile;
import pl.sm_projekt_aplikacjatodo.ProfileWithTasks;

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
    LiveData<List<ProfileWithTasks>> findAllProfilesWithTasks();

    @Transaction
    @Query("SELECT * FROM Profile WHERE profileId = :profileId")
    ProfileWithTasks findProfileWithTasksByProfileId(int profileId);
}