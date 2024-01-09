package pl.sm_projekt_aplikacjatodo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private long taskId;
    private String title;
    private String description;
    //TU MUSI BC TYPE CONVERTER BO BITMAPY NIE ZAPISZESZ DO BAZY
    //private LocalDateTime dateTime;
    private boolean notify;
    private boolean done;
    private long taskOwnerId;


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getTaskOwnerId() {
        return taskOwnerId;
    }

    public void setTaskOwnerId(long taskOwnerId) {
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

//    public LocalDateTime getDateTime() {
//        return dateTime;
//    }

//    public void setDateTime(LocalDateTime dateTime) {
//        this.dateTime = dateTime;
//    }

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
