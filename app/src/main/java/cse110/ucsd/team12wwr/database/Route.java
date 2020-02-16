package cse110.ucsd.team12wwr.database;

import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity
public class Route {
    @PrimaryKey @NonNull @ColumnInfo(collate = ColumnInfo.NOCASE)
    public String name;

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    public String startingPoint;

    @ColumnInfo(collate = ColumnInfo.NOCASE)
    public String endingPoint;

    @TypeConverters(RouteType.class)
    public RouteType routeType;
    
    @TypeConverters(Hilliness.class)
    public Hilliness hilliness;
    
    @TypeConverters(SurfaceType.class)
    public SurfaceType surfaceType;
    
    @TypeConverters(Evenness.class)
    public Evenness evenness;
    
    @TypeConverters(Difficulty.class)
    public Difficulty difficulty;

    public String notes;

    @TypeConverters(Favorite.class)
    public Favorite favorite;

    public enum RouteType {
        LOOP(0),
        OUT_AND_BACK(1);
        
        private int code;

        RouteType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @TypeConverter
        public static RouteType getRouteType(int code) {
            for(RouteType r : values()) {
                if (r.code == code) {
                    return r;
                }
            }
            return null;
        }

        @TypeConverter
        public static int getEnumCode(RouteType r) {
            if (r == null) {
                return -1;
            }

            return r.code;
        }
    }

    public enum Hilliness {
        FLAT(0),
        HILLY(1);
        
        private int code;

        Hilliness(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
        
        @TypeConverter
        public static Hilliness getHilliness(int code) {
            for(Hilliness h : values()) {
                if (h.code == code) {
                    return h;
                }
            }
            return null;
        }

        @TypeConverter
        public static int getEnumCode(Hilliness h) {
            if (h == null) {
                return -1;
            }

            return h.code;
        }
    }

    public enum SurfaceType {
        STREETS(0),
        TRAIL(1);
        
        private int code;

        SurfaceType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
        
        @TypeConverter
        public static SurfaceType getSurfaceType(int code) {
            for(SurfaceType s : values()) {
                if (s.code == code) {
                    return s;
                }
            }
            return null;
        }

        @TypeConverter
        public static int getEnumCode(SurfaceType s) {
            if (s == null) {
                return -1;
            }

            return s.code;
        }
    }

    public enum Evenness {
        EVEN_SURFACE(0),
        UNEVEN_SURFACE(1);
        
        private int code;

        Evenness(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
        
        @TypeConverter
        public static Evenness getEvenness(int code) {
            for(Evenness e : values()) {
                if (e.code == code) {
                    return e;
                }
            }
            return null;
        }

        @TypeConverter
        public static int getEnumCode(Evenness e) {
            if (e == null) {
                return -1;
            }

            return e.code;
        }
    }

    public enum Difficulty {
        EASY(0),
        MODERATE(1),
        DIFFICULT(2);
        
        private int code;

        Difficulty(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
        
        @TypeConverter
        public static Difficulty getDifficulty(int code) {
            for(Difficulty d : values()) {
                if (d.code == code) {
                    return d;
                }
            }
            return null;
        }


        @TypeConverter
        public static int getEnumCode(Difficulty d) {
            if (d == null) {
                return -1;
            }

            return d.code;
        }
    }

    public enum Favorite {
        FAVORITE(0),
        NOT_FAVORITE(1);

        private int code;

        Favorite(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @TypeConverter
        public static Favorite getFavorite(int code) {
            for(Favorite f : values()) {
                if (f.code == code) {
                    return f;
                }
            }
            return null;
        }

        @TypeConverter
        public static int getEnumCode(Favorite f) {
            if (f == null) {
                return -1;
            }

            return f.code;
        }
    }
}

