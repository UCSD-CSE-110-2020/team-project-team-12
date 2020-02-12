package cse110.ucsd.team12wwr;

import android.os.SystemClock;

public class DeviceClock implements IClock {
    @Override
    public int getCurrentClock() {
        return (int)(SystemClock.elapsedRealtime() / 1000);
    }

    @Override
    public long getCurrentClockMillisecond() {
        return SystemClock.elapsedRealtime();
    }
}
