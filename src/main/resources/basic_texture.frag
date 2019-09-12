#version 300 es
precision mediump float;

out vec4 fragColor;

in vec3 vertexColor;
in vec2 textCoords;

uniform sampler2D aTexture;

void main()
{
    fragColor = texture(aTexture, textCoords) * vec4(vertexColor, 1.0);
}
