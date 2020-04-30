attribute vec4 avVertex;
attribute vec4 avVertexCoordinate;
uniform mat4 umTransformMatrix;
varying vec2 vvTextureCoordinate;
void main() {
    vvTextureCoordinate= (umTransformMatrix * avVertexCoordinate).xy;
    gl_Position = avVertex;
}
