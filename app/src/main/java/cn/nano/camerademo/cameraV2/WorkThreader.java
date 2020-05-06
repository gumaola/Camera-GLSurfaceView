package cn.nano.camerademo.cameraV2;

import android.os.Handler;
import android.os.HandlerThread;

public class WorkThreader extends HandlerThread {
    private Handler workHandler;

    public WorkThreader(String name) {
        super(name);
    }

    @Override
    public synchronized void start() {
        super.start();
        if (workHandler == null)
            workHandler = new Handler(getLooper());
    }

    public Handler getBindHandler() {
        return workHandler;
    }

    public void release() {
        quit();
        if (workHandler != null) {
            workHandler = null;
        }
    }
}
