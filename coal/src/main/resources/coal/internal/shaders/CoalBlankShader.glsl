#version 330

layout (location=0) in vec3 position;
uniform int mode;

uniform mat4 projection;

void main()
{
    if(mode == 2) {
        vec4 worldPosistion = vec4(position,1);
        gl_Position = worldPosistion * projection;
    } else {
        gl_Position = vec4(position, 1.0);
    }
}
// ### END VERTEX-SHADER### //
#version 330

out vec4 fragColor;

void main()
{
    fragColor = vec4(0.2,1.0,0.0,1.0);
}