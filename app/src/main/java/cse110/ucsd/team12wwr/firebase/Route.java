package cse110.ucsd.team12wwr.firebase;

public class Route {
    public String userID;

    public String name;
    public String startingPoint;
    public String endingPoint;
    public RouteType routeType;
    public Hilliness hilliness;
    public SurfaceType surfaceType;
    public Evenness evenness;
    public Difficulty difficulty;
    public String notes;
    public Favorite favorite;

    @Override
    public String toString() {
        return this.name;
    }

    public enum RouteType {
        LOOP,
        OUT_AND_BACK;
    }

    public enum Hilliness {
        FLAT,
        HILLY;
    }

    public enum SurfaceType {
        STREETS,
        TRAIL;
    }

    public enum Evenness {
        EVEN_SURFACE,
        UNEVEN_SURFACE;
    }

    public enum Difficulty {
        EASY,
        MODERATE,
        DIFFICULT;
    }

    public enum Favorite {
        FAVORITE,
        NOT_FAVORITE;
    }
}

