package cse110.ucsd.team12wwr.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomRouteDao {
    @Insert
    void insertAll(RoomRoute... r);

    @Delete
    void delete(RoomRoute r);

    @Update
    void update(RoomRoute r);

    @Query("SELECT * FROM RoomRoute r ORDER BY name ASC")
    List<RoomRoute> retrieveAllRoutes();

    @Query("SELECT * FROM RoomRoute r WHERE r.name=:routeName")
    RoomRoute findName(String routeName);
}
