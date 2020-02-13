package cse110.ucsd.team12wwr;

import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Entity
public class Route {
    @PrimaryKey @NonNull
    public String name;

    public String startingPoint;

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
            return d.code;
        }
    }
}

