#version 300 es
precision mediump float;

out vec4 fragColor;

in vec3 vertexColor;
in vec2 textCoords;

uniform sampler2D texture1;
uniform sampler2D texture2;

void main()
{
    vec2 flippedTexture = vec2(textCoords.x, -textCoords.y); // flip face because of levogiro
    fragColor = mix(texture(texture1, textCoords), texture(texture2, flippedTexture), 0.5) * vec4(vertexColor, 1.0);
}
