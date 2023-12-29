package pl.sm_projekt_aplikacjatodo;

import android.app.Application;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

public class ProfileRepository {

    private final ProfileDAO profileDAO;

    ProfileRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        profileDAO = database.profileDAO();
    }


    void insert(Profile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDAO.insert(profile));
    }

    void delete(Profile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDAO.delete(profile));
    }

    void update(Profile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDAO.update(profile));
    }

    void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(profileDAO::deleteAll);
    }

    public List<ProfileWithTasks> findAllProfilesWithTasks() {
        return profileDAO.findAllProfilesWithTasks();
    }

    public ProfileWithTasks findProfileWithTasksByProfileId(int profileId) {
        return profileDAO.findProfileWithTasksByProfileId(profileId);
    }
}
