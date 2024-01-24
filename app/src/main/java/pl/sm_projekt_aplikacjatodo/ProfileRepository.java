package pl.sm_projekt_aplikacjatodo;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

public class ProfileRepository {

    private final ProfileDAO profileDAO;
    private final LiveData<List<ProfileWithTasks>> profiles;

    ProfileRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        profileDAO = database.profileDAO();
        profiles = profileDAO.findAllProfilesWithTasks();
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

    public LiveData<List<ProfileWithTasks>> findAllProfilesWithTasks() {
        return profiles;
    }

    public ProfileWithTasks findProfileWithTasksByProfileId(int profileId) {
        return profileDAO.findProfileWithTasksByProfileId(profileId);
    }
}
