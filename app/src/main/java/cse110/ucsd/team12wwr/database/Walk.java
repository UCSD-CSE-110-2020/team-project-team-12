package cse110.ucsd.team12wwr.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Route.class,
        parentColumns = "name",
        childColumns = "routeName",
        onDelete = ForeignKey.CASCADE))
public class Walk {
    @PrimaryKey
    public long time;

    public String routeName;
    public String duration;
    public String steps;
    public String distance;
}
