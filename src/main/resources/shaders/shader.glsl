# VERTEX_SHADER
#version 400 core

layout(location = 0) in vec3 position;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = vec4(projection * view * model * vec4(position, 1.0f));
}

# FRAGMENT_SHADER
#version 400 core

out vec4 out_color;

void main()
{
	out_color = vec4(0.5f, 0.5f, 0.5f, 1.0f);
}