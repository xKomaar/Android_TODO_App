package pl.sm_projekt_aplikacjatodo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
public class Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Integer taskId;
    private String title;
    private String description;
    private String dateTime;
    private boolean done;
    private Integer taskOwnerId;

    public Task() {

    }

    public Task(String title, String description, String dateTime, boolean done, int taskOwnerId) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.done = done;
        this.taskOwnerId = taskOwnerId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskOwnerId() {
        return taskOwnerId;
    }

    public void setTaskOwnerId(int taskOwnerId) {
        this.taskOwnerId = taskOwnerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public LocalDateTime getLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.US);
        return LocalDateTime.parse(dateTime, formatter);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime.toString();
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    //metody implementujące Parcable (po to, zeby w liscie tasków móc zapisać instance)
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
