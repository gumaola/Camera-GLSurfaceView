package cn.nano.camerademo.engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class EnginePreview extends GLSurfaceView {
    public EnginePreview(Context context) {
        this(context, null);
    }

    public EnginePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPreview();
    }

    private void initPreview() {
        setEGLContextClientVersion(2);
    }
}
