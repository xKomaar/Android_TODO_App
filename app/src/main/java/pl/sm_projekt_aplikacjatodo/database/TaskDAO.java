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

import pl.sm_projekt_aplikacjatodo.model.Profile;
import pl.sm_projekt_aplikacjatodo.model.Task;

@Dao
public interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);
    @Transaction
    @Query("DELETE FROM Task")
    void deleteAll();
    @Transaction
    @Query("SELECT * FROM Task")
    LiveData<List<Task>> findAll();
    @Transaction
    @Query("SELECT * FROM Task WHERE taskOwnerId = :taskOwnerId")
    LiveData<List<Task>> findAllByTaskOwnerId(int taskOwnerId);

    @Transaction
    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    LiveData<Task> findTaskByTaskId(int taskId);
}