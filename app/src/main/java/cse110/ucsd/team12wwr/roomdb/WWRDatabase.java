package cse110.ucsd.team12wwr.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Walk.class, Route.class}, version = 2)
public abstract class WWRDatabase extends RoomDatabase {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // added favorite enum
            database.execSQL("ALTER TABLE route ADD COLUMN favorite INTEGER");
        }
    };

    private static volatile WWRDatabase INSTANCE;
    public abstract WalkDao walkDao();
    public abstract RouteDao routeDao();

    public static WWRDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (WWRDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WWRDatabase.class, "wwr_database")
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
