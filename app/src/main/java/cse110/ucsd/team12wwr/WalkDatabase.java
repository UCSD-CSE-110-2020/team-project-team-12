package cse110.ucsd.team12wwr;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Walk.class}, version = 1, exportSchema = false)
public abstract class WalkDatabase extends RoomDatabase {
    private static volatile WalkDatabase INSTANCE;
    public abstract WalkDao walkDao();

    public static WalkDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WalkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WalkDatabase.class, "word_database").build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
