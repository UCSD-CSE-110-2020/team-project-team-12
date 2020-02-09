package cse110.ucsd.team12wwr;

public class JavaClock implements iClock {
    @Override
    public int getCurrentClock() {
        return (int)(System.currentTimeMillis() / 1000);
    }

    @Override
    public long getCurrentClockMillisecond() {
        return System.currentTimeMillis();
    }
}
