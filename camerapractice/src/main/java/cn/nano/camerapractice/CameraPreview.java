package cn.nano.camerapractice;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraPreview extends GLSurfaceView implements SurfaceTexture.OnFrameAvailableListener {
    public CameraPreview(Context context) {
        this(context, null);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initGL(SurfaceTexture texture) {
        //设置使用open gl es2
        setEGLContextClientVersion(2);

        //设置渲染器
        CameraRenderer renderer = new CameraRenderer(texture);
        setRenderer(renderer);

        //设置渲染模式
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //实现于SurfaceTexture回调，用于接收数据帧缓冲完成
        requestRender();
    }

    public static class CameraRenderer implements Renderer {

        private SurfaceTexture texture;
        //用于接收texture的变换矩阵，传入着色器进行图像调整
        private float[] transformMatrix = new float[16];

        public CameraRenderer(SurfaceTexture t) {
            texture = t;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            texture.updateTexImage();
            texture.getTransformMatrix(transformMatrix);
            
        }
    }
}
