package cse110.ucsd.team12wwr.firebase;

import cse110.ucsd.team12wwr.MainActivity;

public class DaoFactory {
    public static boolean unitTestFlag = MainActivity.unitTestFlag;

    public static InvitationDao getInvitationDao() {
        if (!unitTestFlag) {
            return new FirebaseInvitationDao();
        } else {
            return new MockInvitationDao();
        }
    }

    public static RouteDao getRouteDao() {
        if (!unitTestFlag) {
            return new FirebaseRouteDao();
        } else {
            return new MockRouteDao();
        }
    }

    public static ScheduleDao getScheduleDao() {
        if (!unitTestFlag) {
            return new FirebaseScheduleDao();
        } else {
            return new MockScheduleDao();
        }
    }

    public static UserDao getUserDao() {
        if (!unitTestFlag) {
            return new FirebaseUserDao();
        } else {
            return new MockUserDao();
        }
    }

    public static WalkDao getWalkDao() {
        if (!unitTestFlag) {
            return new FirebaseWalkDao();
        } else {
            return new MockWalkDao();
        }
    }
}
