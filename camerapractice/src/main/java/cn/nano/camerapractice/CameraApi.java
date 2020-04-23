package cn.nano.camerapractice;

import android.hardware.Camera;
import android.util.SparseArray;

import java.util.List;

public class CameraApi {

    private int frontCameraId;
    private int backCameraId;

    private SparseArray<Camera.CameraInfo> allCameraInfos;

    public CameraApi() {
        checkDeviceInfo();
    }

    private void checkDeviceInfo() {
        //获取camera数量
        int numbers = Camera.getNumberOfCameras();
        if (numbers <= 0) {
            throw new UnsupportedOperationException("This device have no camera!");
        }

        allCameraInfos = new SparseArray<>();
        for (int i = 0; i < numbers; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            allCameraInfos.put(i, info);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCameraId = i;
                continue;
            }

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backCameraId = i;
            }
        }
    }


    public Camera openFrontCamera() {
        Camera camera = Camera.open(frontCameraId);
        Camera.Parameters parameters = camera.getParameters();
        parameters.set("orientation", "portrait");
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setPreviewSize(1280, 720);
        camera.setParameters(parameters);

        return camera;
    }

    public Camera openBackCamera() {
        Camera camera = Camera.open(backCameraId);
        Camera.Parameters parameters = camera.getParameters();
        parameters.set("orientation", "portrait");
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setPreviewSize(1280, 720);
        camera.setParameters(parameters);

        return camera;
    }

    public void previewBy(Camera camera) {
        camera.startPreview();
    }

    public void stopPreview(Camera camera) {
        camera.stopPreview();
    }

    public void close(Camera camera) {
        camera.release();
    }


}
