package cse110.ucsd.team12wwr.clock;

public class JavaClock implements IClock {
    @Override
    public int getCurrentClock() {
        return (int)(System.currentTimeMillis() / 1000);
    }

    @Override
    public long getCurrentClockMillisecond() {
        return System.currentTimeMillis();
    }
}
