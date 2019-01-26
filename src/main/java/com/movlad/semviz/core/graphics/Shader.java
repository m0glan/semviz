package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

class Shader {

    private int id;
    private final int type;
    private final String[] source;

    public Shader(InputStream is, int type) throws IOException {
        this.type = type;
        this.source = new String[]{""};

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;

            while ((line = reader.readLine()) != null) {
                this.source[0] += line + "\r\n";
            }
        }
    }

    public int getId() {
        return id;
    }

    public void compile(GL3 gl) {
        int tempId = gl.glCreateShader(type);

        gl.glShaderSource(tempId, 1, source, null);
        gl.glCompileShader(tempId);

        IntBuffer success = IntBuffer.allocate(1);

        gl.glGetShaderiv(tempId, GL3.GL_COMPILE_STATUS, success);

        if (success.get(0) == GL3.GL_FALSE) {
            IntBuffer infoLogLength = IntBuffer.allocate(1);

            gl.glGetShaderiv(tempId, GL3.GL_INFO_LOG_LENGTH, infoLogLength);

            ByteBuffer infoLog = ByteBuffer.allocate(infoLogLength.get(0));

            gl.glGetShaderInfoLog(tempId, infoLogLength.get(0), infoLogLength, infoLog);

            String errorMessage = new String(infoLog.array(), Charset.forName("UTF-8"));

            throw new GLException(errorMessage);
        }

        this.id = tempId;
    }

    public void delete(GL3 gl) {
        gl.glDeleteShader(id);
    }

}
