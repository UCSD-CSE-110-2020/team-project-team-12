package cse110.ucsd.team12wwr.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoomWalkDao {
    @Insert
    void insertAll(RoomWalk... w);

    @Delete
    void delete(RoomWalk w);

    @Query("SELECT * FROM RoomWalk w WHERE NOT EXISTS (SELECT a.time FROM RoomWalk a WHERE a.time > w.time)")
    RoomWalk findNewestEntry();

    @Query("SELECT * FROM RoomWalk w WHERE w.routeName=:routeName ORDER BY time DESC")
    List<RoomWalk> findByRouteName(String routeName);
}

