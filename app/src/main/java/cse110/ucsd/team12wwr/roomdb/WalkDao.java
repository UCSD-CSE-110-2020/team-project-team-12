package cse110.ucsd.team12wwr.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WalkDao {
    @Insert
    void insertAll(Walk... w);

    @Delete
    void delete(Walk w);

    @Query("SELECT * FROM Walk w WHERE NOT EXISTS (SELECT a.time FROM Walk a WHERE a.time > w.time)")
    Walk findNewestEntry();

    @Query("SELECT * FROM Walk w WHERE w.routeName=:routeName ORDER BY time DESC")
    List<Walk> findByRouteName(String routeName);
}

