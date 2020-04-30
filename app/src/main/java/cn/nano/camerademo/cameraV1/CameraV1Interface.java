package cn.nano.camerademo.cameraV1;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.List;

public class CameraV1Interface {

    private Context context;
    private SurfaceTexture surfaceTexture;
    private Camera opendCamera;

    public CameraV1Interface(Activity c, int facing) {
        context = c;
        initCamera(c, facing);
    }

    private boolean isInPreviewing;

    public void startPreviewOnTexture(SurfaceTexture texture) {
        if (isInPreviewing) {
            return;
        }
        surfaceTexture = texture;
        try {
            opendCamera.setPreviewTexture(texture);
            opendCamera.startPreview();
            isInPreviewing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        try {
            opendCamera.release();
        } catch (Exception e) {

        }
    }

    private void initCamera(Activity activity, int facing) {
        int ids = Camera.getNumberOfCameras();
        if (ids < 0) {
            return;
        }


        for (int i = 0; i < ids; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == facing) {
                openCamera(activity, i);
                break;
            }
        }
    }

    private void openCamera(Activity activity, int id) {
        try {
            opendCamera = Camera.open(id);
            Camera.Parameters parameters = opendCamera.getParameters();
            parameters.set("orientation", "portrait");
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setPreviewSize(1280, 720);
            setCameraDisplayOrientation(activity, id, opendCamera);
            opendCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private Camera.Size filteBestPreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = 0;
        int screenHeight = 0;
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            display.getMetrics(metrics);
            screenWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
            screenHeight = Math.max(metrics.widthPixels, metrics.heightPixels);
        }

        if (screenWidth == 0 || screenHeight == 0) {
            return null;
        }

        int bestWidth = 0;
        int bestHeight = 0;
        Camera.Size bestSize = null;
        for (Camera.Size size : sizes) {
            if (size.width >= screenWidth//预览宽大于输出宽
                    && size.height >= screenHeight//预览高大于输出高
                    && (size.width * size.height < bestWidth * bestHeight || 0 == bestWidth * bestHeight)) {//选择像素最少的分辨率
                bestWidth = size.width;
                bestHeight = size.height;
                bestSize = size;
            }
        }

        return bestSize;
    }

}
