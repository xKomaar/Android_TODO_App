package pl.sm_projekt_aplikacjatodo.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import pl.sm_projekt_aplikacjatodo.model.Profile;
import pl.sm_projekt_aplikacjatodo.model.ProfileWithTasks;

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

    public LiveData<List<Profile>> findAllProfiles() {
        return profileDAO.findAllProfiles();
    }

    public ProfileWithTasks findProfileWithTasksByProfileId(int profileId) {
        return profileDAO.findProfileWithTasksByProfileId(profileId);
    }

    public LiveData<Profile> findProfileByProfileId(int profileId) {
        return profileDAO.findProfileByProfileId(profileId);
    }
}
