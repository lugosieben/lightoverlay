// @SuppressInspection All
#version 330
#moj_import <minecraft:fog.glsl>

// dynamicimports.glsl
layout(std140) uniform DynamicTransforms {
    mat4 ModelViewMat;
    vec4 ColorModulator;
    vec3 ModelOffset;
    mat4 TextureMat;
};
// projection.glsl
layout(std140) uniform Projection {
    mat4 ProjMat;
};

in vec3 Position;
in vec2 UV0;
in vec4 Color;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec2 texCoord0;
out vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
    texCoord0 = UV0;
    vertexColor = Color;
}