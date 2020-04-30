package cn.nano.camerademo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import cn.nano.camerademo.cameraV1.CameraV1Interface;
import cn.nano.camerademo.engine.EmptyEngine;
import cn.nano.camerademo.engine.EnginePreview;
import cn.nano.camerademo.engine.Utils;

public class CameraActivity extends Activity {
    public static final String api_version = "api_version";

    private EnginePreview preview;
    private CameraV1Interface camera;
    private EmptyEngine engine;

    //统一调用
    public static void forward(Context context, int api) {
        Intent i = new Intent(context, CameraActivity.class);
        i.putExtra(api_version, api);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        preview = findViewById(R.id.preview);


        //创建camera api
        camera = new CameraV1Interface(this, Camera.CameraInfo.CAMERA_FACING_BACK);

        //创建engine
        engine = new EmptyEngine(this, camera, preview);
        preview.setRenderer(engine);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (camera != null) {
            camera.stop();
        }
        if (engine != null) {
            engine.release();
        }
    }
}
