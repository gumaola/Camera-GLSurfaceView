#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES usTextureOes;
varying vec2 vvTextureCoordinate;
void main() {
    vec4 vCameraColor = texture2D(usTextureOes, vvTextureCoordinate);
    float fGrayColor = (0.3*vCameraColor.r + 0.59*vCameraColor.g + 0.11*vCameraColor.b);
    gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
}
