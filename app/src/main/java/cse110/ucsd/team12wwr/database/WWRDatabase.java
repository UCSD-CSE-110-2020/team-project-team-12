package cse110.ucsd.team12wwr.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Walk.class, Route.class}, version = 1, exportSchema = false)
public abstract class WWRDatabase extends RoomDatabase {
    private static volatile WWRDatabase INSTANCE;
    public abstract WalkDao walkDao();
    public abstract RouteDao routeDao();

    public static WWRDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WWRDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WWRDatabase.class, "word_database").build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
