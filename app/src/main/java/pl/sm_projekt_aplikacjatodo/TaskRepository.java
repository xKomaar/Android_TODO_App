package pl.sm_projekt_aplikacjatodo;

import android.app.Application;

import androidx.room.Query;

import java.util.List;

public class TaskRepository {

    private final TaskDAO taskDAO;

    TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        taskDAO = database.taskDAO();
    }

    void insert(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskDAO.insert(task));
    }

    void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskDAO.delete(task));
    }

    void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskDAO.update(task));
    }

    void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(taskDAO::deleteAll);
    }

    public List<Task> findAll() {
        return taskDAO.findAll();
    }

    public List<Task> findByTaskOwnerId(int taskOwnerId) {
        return taskDAO.findByTaskOwnerId(taskOwnerId);
    }
}