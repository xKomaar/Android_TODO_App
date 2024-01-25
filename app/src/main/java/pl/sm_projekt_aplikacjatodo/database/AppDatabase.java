package pl.sm_projekt_aplikacjatodo.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.sm_projekt_aplikacjatodo.model.Profile;
import pl.sm_projekt_aplikacjatodo.model.Task;

@Database(entities = {Profile.class, Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    public abstract ProfileDAO profileDAO();
    public abstract TaskDAO taskDAO();

    public static AppDatabase getDatabase(final Context context) {
        if(databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "todo_app_database")
                    .addCallback(databaseCallback)
                    .build();
        }
        return databaseInstance;
    }

    private static final RoomDatabase.Callback databaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                ProfileDAO profileDAO = databaseInstance.profileDAO();
                TaskDAO taskDAO = databaseInstance.taskDAO();
                Profile profile = new Profile("testowy");
                profileDAO.insert(profile);
                Task task = new Task("tytul1", "opis1", "17.01.2024", false, false, 1);
                taskDAO.insert(task);
                task = new Task("tytul2", "opis2", "18.01.2024", false, false, 1);
                taskDAO.insert(task);
                profile = new Profile("testowy 1");
                profileDAO.insert(profile);
                task = new Task("tytul3", "opis3", "19.01.2024", false, false, 2);
                taskDAO.insert(task);
                task = new Task("tytul4", "opis4", "20.01.2024", false, false, 2);
                taskDAO.insert(task);

            });
        }
    };
}
