package cse110.ucsd.team12wwr.fitness;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import cse110.ucsd.team12wwr.MainActivity;
import cse110.ucsd.team12wwr.R;

public class FitnessServiceFactory {

    private static final String TAG = "[FitnessServiceFactory]";
    //MainActivity mainAct ;

    private static Map<String, BluePrint> blueprints = new HashMap<>();

    public static void put(String key, BluePrint bluePrint) {
        blueprints.put(key, bluePrint);
    }

    public static FitnessService create(String key, MainActivity mainActivity) {
        Log.i(TAG, String.format("creating FitnessService with key %s", key));
        //setContentView(R.layout.activity_main);

        return blueprints.get(key).create(mainActivity);
    }

    public interface BluePrint {
        FitnessService create(MainActivity mainActivity);
    }
}
