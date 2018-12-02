# VERTEX_SHADER
#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 v_color;

out vec4 f_color;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = vec4(projection * view * model * vec4(position, 1.0f));
    f_color = vec4(v_color, 1.0f);
}

# FRAGMENT_SHADER
#version 400 core

in vec4 f_color;

out vec4 out_color;

void main()
{
	out_color = f_color;
}