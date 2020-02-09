package cse110.ucsd.team12wwr;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Walk {
    @PrimaryKey
    public long time;

    public String duration;
    public String steps;
    public String distance;
}
