/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class ShaderProgram {

    private final List<Shader> shaders;
    private int id;

    public ShaderProgram() {
        shaders = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void push(Shader shader) {
        shaders.add(shader);
    }

    public void link(GL3 gl) {
        int tempId = gl.glCreateProgram();

        shaders.forEach(shader -> {
            shader.compile(gl);
            gl.glAttachShader(tempId, shader.getId());
        });

        gl.glLinkProgram(tempId);
        gl.glValidateProgram(tempId);

        shaders.forEach(shader -> {
            shader.delete(gl);
        });

        IntBuffer linkSuccess = IntBuffer.allocate(1);
        IntBuffer valid = IntBuffer.allocate(1);

        gl.glGetProgramiv(tempId, GL3.GL_LINK_STATUS, linkSuccess);
        gl.glGetProgramiv(tempId, GL3.GL_VALIDATE_STATUS, valid);

        if (linkSuccess.get(0) == GL3.GL_FALSE || valid.get(0) == GL3.GL_FALSE) {
            IntBuffer infoLogLength = IntBuffer.allocate(1);

            gl.glGetProgramiv(tempId, GL3.GL_INFO_LOG_LENGTH, infoLogLength);

            ByteBuffer infoLog = ByteBuffer.allocate(infoLogLength.get(0));

            gl.glGetProgramInfoLog(tempId, infoLogLength.get(0), infoLogLength, infoLog);

            String errorMessage = new String(infoLog.array(), Charset.forName("UTF-8"));

            throw new GLException(errorMessage);
        }

        this.id = tempId;
    }

    public void use(GL3 gl) {
        gl.glUseProgram(id);
    }

    public void delete(GL3 gl) {
        gl.glDeleteProgram(id);
    }

}
