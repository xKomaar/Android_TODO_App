package pl.sm_projekt_aplikacjatodo;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import java.util.List;

public class TaskRepository {

    private final TaskDAO taskDAO;
    private final LiveData<List<Task>> tasks;

    TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        taskDAO = database.taskDAO();
        tasks = taskDAO.findAll();
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

    public LiveData<List<Task>> findAll() {
        return tasks;
    }

    public List<Task> findByTaskOwnerId(int taskOwnerId) {
        return taskDAO.findByTaskOwnerId(taskOwnerId);
    }
}