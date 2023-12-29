package pl.sm_projekt_aplikacjatodo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Profile.class, Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    public abstract ProfileDAO profileDAO();
    public abstract TaskDAO taskDAO();

    static AppDatabase getDatabase(final Context context) {
        if(databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "todo_app_database")
                    .build();
        }
        return databaseInstance;
    }
}
