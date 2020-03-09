package cse110.ucsd.team12wwr.roomdb;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = RoomRoute.class,
        parentColumns = "name",
        childColumns = "routeName",
        onDelete = ForeignKey.CASCADE))
public class RoomWalk {
    @PrimaryKey @NonNull
    public long time;

    @NonNull
    public String routeName;

    public String duration;
    public String steps;
    public String distance;
}
