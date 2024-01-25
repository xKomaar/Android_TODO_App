package pl.sm_projekt_aplikacjatodo.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pl.sm_projekt_aplikacjatodo.model.Profile;
import pl.sm_projekt_aplikacjatodo.model.Task;

public class ProfileWithTasks {
    @Embedded
    public Profile profile;
    @Relation(parentColumn = "profileId", entityColumn = "taskOwnerId")
    public List<Task> taskList;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
