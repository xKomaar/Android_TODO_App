package pl.sm_projekt_aplikacjatodo.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.sm_projekt_aplikacjatodo.Profile;
import pl.sm_projekt_aplikacjatodo.Task;

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
                Profile profile = new Profile("testowy");
                profileDAO.insert(profile);
                profile = new Profile("testowy 1");
                profileDAO.insert(profile);
            });
        }
    };
}
