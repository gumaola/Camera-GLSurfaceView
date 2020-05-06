package cn.nano.camerademo.base;

import android.graphics.SurfaceTexture;

public interface ICamera {

    void startPreviewOnTexture(SurfaceTexture texture);

    void stop();
}
