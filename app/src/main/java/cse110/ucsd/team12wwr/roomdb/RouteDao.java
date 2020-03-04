package cse110.ucsd.team12wwr.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RouteDao {
    @Insert
    void insertAll(Route... r);

    @Delete
    void delete(Route r);

    @Update
    void update(Route r);

    @Query("SELECT * FROM Route r ORDER BY name ASC")
    List<Route> retrieveAllRoutes();

    @Query("SELECT * FROM Route r WHERE r.name=:routeName")
    Route findName(String routeName);
}
