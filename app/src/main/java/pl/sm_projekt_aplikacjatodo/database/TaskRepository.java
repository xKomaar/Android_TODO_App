package pl.sm_projekt_aplikacjatodo.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import pl.sm_projekt_aplikacjatodo.Task;

public class TaskRepository {

    private final TaskDAO taskDAO;
    private final LiveData<List<Task>> tasks;

    public TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        taskDAO = database.taskDAO();
        tasks = taskDAO.findAll();
    }

    public void insert(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskDAO.insert(task));
    }

    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskDAO.delete(task));
    }

    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> taskDAO.update(task));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(taskDAO::deleteAll);
    }

    public LiveData<List<Task>> findAll() {
        return tasks;
    }

    public List<Task> findByTaskOwnerId(int taskOwnerId) {
        return taskDAO.findByTaskOwnerId(taskOwnerId);
    }
}