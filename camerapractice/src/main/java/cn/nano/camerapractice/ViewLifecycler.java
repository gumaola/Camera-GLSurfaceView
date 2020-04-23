package cn.nano.camerapractice;

import android.content.Context;

public interface ViewLifecycler {
    void onCreate(Context context);

    void onStart(Context context);

    void onStop(Context context);

    void onDestory(Context context);
}
