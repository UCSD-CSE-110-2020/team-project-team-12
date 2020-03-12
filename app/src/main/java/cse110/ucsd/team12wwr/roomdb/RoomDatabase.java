package cse110.ucsd.team12wwr.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {RoomWalk.class, RoomRoute.class}, version = 2)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // added favorite enum
            database.execSQL("ALTER TABLE route ADD COLUMN favorite INTEGER");
        }
    };

    private static volatile RoomDatabase INSTANCE;
    public abstract RoomWalkDao walkDao();
    public abstract RoomRouteDao routeDao();

    public static RoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "wwr_database")
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
