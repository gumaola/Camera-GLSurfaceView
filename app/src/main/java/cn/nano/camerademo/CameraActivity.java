package cn.nano.camerademo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class CameraActivity extends Activity {
    public static final String api_version = "api_version";

    //统一调用
    public static void forward(Context context, int api) {
        Intent i = new Intent(context, CameraActivity.class);
        i.putExtra(api_version, api);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
}
