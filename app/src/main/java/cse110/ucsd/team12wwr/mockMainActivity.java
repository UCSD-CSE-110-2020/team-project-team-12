package cse110.ucsd.team12wwr;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class mockMainActivity extends MainActivity {
    public mockMainActivity(){
        //testingFlag = true;
    }
    @Override
    public void setTestingFlag(boolean flag) {
        testingFlag = !flag;
    }

}
