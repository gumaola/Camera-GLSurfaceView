package cn.nano.camerapractice;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CameraFragment extends Fragment {

    private CameraPreview preview;
    private CameraApi cameraApi;

    private Camera openedCamera;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        preview = view.findViewById(R.id.preview);

        //start flow
        CameraApi api = new CameraApi();

        //open
        openedCamera = api.openFrontCamera();

        //bind texture
        SurfaceTexture texture = SurfaceTextureFactory.create();

        try {
            openedCamera.setPreviewTexture(texture);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * 注意此方法也可以获取摄像数据
         */
//        openedCamera.setPreviewCallback(new Camera.PreviewCallback() {
//            @Override
//            public void onPreviewFrame(byte[] data, Camera camera) {
//
//            }
//        });

        //主要为texture设置callback，监听texture数据刷新，然后调用渲染器的刷新
        preview.initGL(texture);

        //preview
        api.previewBy(openedCamera);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraApi.close(openedCamera);
    }
}
