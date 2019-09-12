#version 300 es
precision mediump float;

out vec4 fragColor;

in vec3 vertexColor;

void main()
{
    fragColor = vec4(vertexColor, 1.0);
}
