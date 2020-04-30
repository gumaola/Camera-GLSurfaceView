package cn.nano.camerademo.engine;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cn.nano.camerademo.R;
import cn.nano.camerademo.cameraV1.CameraV1Interface;

import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.glBindFramebuffer;

public class EmptyEngine implements GLSurfaceView.Renderer {

    private Context context;

    private int surfaceTextureId;
    private SurfaceTexture surfaceTexture;//纹理输出对象

    private int programHandle;
    private int vertexHandle;
    private int fragmentHandle;


    private int vertexPositionHandle;
    private int vertexMatrixHandle;
    private int texureOESHandle;
    private int vertexCoordinateHandle;

    private CameraV1Interface opendCamera;

    private GLSurfaceView surfaceView;

    private FloatBuffer vertexBuffer;
    private static final float[] vertex_coords = new float[]{
            1f, 1f, 0,
            -1f, 1f, 0,
            -1f, -1f, 0,
            1f, 1f, 0,
            -1f, -1f, 0,
            1f, -1f, 0
    };

    private FloatBuffer vertexOrederBuffer;
    private static final float[] vertex_coords_order = new float[]{
            1f, 1f,
            0, 1f,
            0, 0,
            1f, 1f,
            0, 0,
            1f, 0
    };


    private float[] transformMatrix = new float[16];

    public EmptyEngine(Context c, CameraV1Interface camera, GLSurfaceView surface) {
        context = c;
        opendCamera = camera;
        surfaceView = surface;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        createSurfaceTexture();

        opendCamera.startPreviewOnTexture(surfaceTexture);

        //创建buffer
        vertexBuffer = ByteBuffer.allocateDirect(vertex_coords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexBuffer.put(vertex_coords).position(0);

        vertexOrederBuffer = ByteBuffer.allocateDirect(vertex_coords_order.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertex_coords_order);
        vertexOrederBuffer.position(0);

        programHandle = GLES20.glCreateProgram();

        vertexHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        String vertexShader = Utils.readShaderFromResource(context, R.raw.vertex_shader);
        GLES20.glShaderSource(vertexHandle, vertexShader);
        GLES20.glCompileShader(vertexHandle);
        GLES20.glAttachShader(programHandle, vertexHandle);

        fragmentHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        String fragmentShader = Utils.readShaderFromResource(context, R.raw.fragment_shader);
        GLES20.glShaderSource(fragmentHandle, fragmentShader);
        GLES20.glCompileShader(fragmentHandle);
        GLES20.glAttachShader(programHandle, fragmentHandle);

        GLES20.glLinkProgram(programHandle);
    }


    private void createSurfaceTexture() {
        if (surfaceTexture != null) {
            return;
        }
        //创建一个surfacetexture
        surfaceTextureId = Utils.createOESTextureObject();
        surfaceTexture = new SurfaceTexture(surfaceTextureId);
        opendCamera.startPreviewOnTexture(surfaceTexture);

        //当camera有数据返回时，刷新
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                surfaceView.requestRender();
            }
        });
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (surfaceTexture != null) {
            surfaceTexture.updateTexImage();
            surfaceTexture.getTransformMatrix(transformMatrix);
        }

        GLES20.glUseProgram(programHandle);

        vertexPositionHandle = GLES20.glGetAttribLocation(programHandle, "avVertex");
        vertexMatrixHandle = GLES20.glGetUniformLocation(programHandle, "umTransformMatrix");
        texureOESHandle = GLES20.glGetUniformLocation(programHandle, "usTextureOes");
        vertexCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "avVertexCoordinate");

        GLES20.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
        GLES20.glVertexAttribPointer(vertexCoordinateHandle, 2, GLES20.GL_FLOAT, false, 8, vertexOrederBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, surfaceTextureId);
        GLES20.glUniform1i(texureOESHandle, 0);

        GLES20.glUniformMatrix4fv(vertexMatrixHandle, 1, false, transformMatrix, 0);

        GLES20.glEnableVertexAttribArray(vertexPositionHandle);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES20.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }


    public void release() {
        if (surfaceTexture != null) {
            surfaceTexture.release();
            surfaceTexture = null;
        }
    }
}
