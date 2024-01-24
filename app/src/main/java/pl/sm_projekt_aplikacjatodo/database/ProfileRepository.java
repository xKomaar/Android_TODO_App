package pl.sm_projekt_aplikacjatodo.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import pl.sm_projekt_aplikacjatodo.Profile;
import pl.sm_projekt_aplikacjatodo.ProfileWithTasks;

public class ProfileRepository {

    private final ProfileDAO profileDAO;

    public ProfileRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        profileDAO = database.profileDAO();
    }


    public void insert(Profile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDAO.insert(profile));
    }

    public void delete(Profile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDAO.delete(profile));
    }

    public void update(Profile profile) {
        AppDatabase.databaseWriteExecutor.execute(() -> profileDAO.update(profile));
    }

    public LiveData<List<ProfileWithTasks>> findAllProfilesWithTasks() {
        return profileDAO.findAllProfilesWithTasks();
    }

    public ProfileWithTasks findProfileWithTasksByProfileId(int profileId) {
        return profileDAO.findProfileWithTasksByProfileId(profileId);
    }
}
