#version 300 es
layout(location = 0) in vec3 Position;
layout(location = 1) in vec3 SourceColor;
layout(location = 2) in vec2 UVCoords;

out vec3 vertexColor;
out vec2 textCoords;

void main()
{
    gl_Position = vec4(Position, 1.0);
    vertexColor = SourceColor;
    textCoords  = UVCoords;
}
