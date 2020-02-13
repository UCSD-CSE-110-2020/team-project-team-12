package cse110.ucsd.team12wwr;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteDao {
    @Insert
    void insertAll(Route... r);

    @Delete
    void delete(Route r);

    @Query("SELECT * FROM Route r ORDER BY name ASC")
    List<Route> retrieveAllRoutes();
}
