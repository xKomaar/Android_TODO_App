package pl.sm_projekt_aplikacjatodo.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private Integer taskId;
    private String title;
    private String description;
    private String dateTime;
    private boolean notify;
    private boolean done;
    private Integer taskOwnerId;

    public Task(String title, String description, String dateTime, boolean notify, boolean done, int taskOwnerId) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.notify = notify;
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
        return LocalDateTime.parse(dateTime);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime.toString();
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
