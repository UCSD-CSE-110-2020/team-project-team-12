package cse110.ucsd.team12wwr.fitness;

public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateStepCount();
    boolean getSubscribed();
    long getStepValue();
}
