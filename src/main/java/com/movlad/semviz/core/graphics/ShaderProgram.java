package com.movlad.semviz.core.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLException;
import java.io.InputStream;

class ShaderSource {
	public String[] vertexShaderSource = { "" };
	public String[] fragmentShaderSource = { "" };
}

enum ShaderType { NONE, VERTEX_SHADER, FRAGMENT_SHADER }

/**
 * Shader program that runs on the GPU. Compiled from the sources for a vertex shader
 * and a fragment shader.
 */
public class ShaderProgram {

    private int id;
    private GL4 gl;

    /**
     * Constructor. 
     * 
     * @param gl is the context
     * @param vertexShaderSource is the GLSL source for the vertex shader
     * @param fragmentShaderSource is the GLSL source for the fragment shader
     * @throws GLException upon compilation, linkage or validation errors.
     */
    public ShaderProgram(GL4 gl, String[] vertexShaderSource, String[] fragmentShaderSource) 
                    throws GLException {
        this.gl = gl;
        this.id = createProgram(vertexShaderSource, fragmentShaderSource);
    }

    public ShaderProgram(GL4 gl, InputStream is) throws IOException {
        this.gl = gl;

        ShaderSource source = parse(is);

        this.id = createProgram(source.vertexShaderSource, source.fragmentShaderSource);
    }

    public int getId() { return id; }

    /**
     * Sets this shader as the currently used program.
     */
    public void use() {
        gl.glUseProgram(id);
    }

    /**
     * Deletes the shader program.
     */
    public void delete() {
        gl.glDeleteProgram(id);
    }

    /**
     * Compiles a shader component of the program.
     * 
     * @param type is the type of shader, {@code GL_VERTEX_SHADER} or {@code GL_FRAGMENT_SHADER}
     * @param source is the GLSL source code for the shader
     * @return the id of the created shader if no exception was raised
     * @throws GLException if the shader fails to compile
     */
    private int compileShader(int type, String[] source) throws GLException {
        int id = gl.glCreateShader(type);

        gl.glShaderSource(id, 1, source, null);
        gl.glCompileShader(id);

        IntBuffer success = IntBuffer.allocate(1);

        gl.glGetShaderiv(id, GL4.GL_COMPILE_STATUS, success);

        if (success.get(0) == GL4.GL_FALSE) {
            IntBuffer infoLogLength = IntBuffer.allocate(1);

            gl.glGetShaderiv(id, GL4.GL_INFO_LOG_LENGTH, infoLogLength);

            ByteBuffer infoLog = ByteBuffer.allocate(infoLogLength.get(0));

            gl.glGetShaderInfoLog(id, infoLogLength.get(0), infoLogLength, infoLog);

            String errorMessage = new String(infoLog.array(), Charset.forName("UTF-8"));

            throw new GLException(errorMessage);
        }

        return id;
    }

    /**
     * Links the vertex and fragment shaders to the program.
     * 
     * @param vertexShaderSource is the GLSL source for the vertex shader
     * @param fragmentShaderSource is the GLSL source for the fragment shader
     * @return the id of the created program
     * @throws GLException upon shader failed shader compilation, linkage or program validation
     */
    private int createProgram(String[] vertexShaderSource, String[] fragmentShaderSource) 
                    throws GLException {
        int vertexShader = compileShader(GL4.GL_VERTEX_SHADER, vertexShaderSource);
        int fragmentShader = compileShader(GL4.GL_FRAGMENT_SHADER, fragmentShaderSource);
        int id = gl.glCreateProgram();

        gl.glAttachShader(id, vertexShader);
        gl.glAttachShader(id, fragmentShader);
        gl.glLinkProgram(id);
        gl.glValidateProgram(id);
        gl.glDeleteShader(vertexShader);
        gl.glDeleteShader(fragmentShader);

        IntBuffer linkSuccess = IntBuffer.allocate(1);
        IntBuffer valid = IntBuffer.allocate(1);

        gl.glGetProgramiv(id, GL4.GL_LINK_STATUS, linkSuccess);
        gl.glGetProgramiv(id, GL4.GL_VALIDATE_STATUS, valid);

        if (linkSuccess.get(0) == GL4.GL_FALSE || valid.get(0) == GL4.GL_FALSE) {
            IntBuffer infoLogLength = IntBuffer.allocate(1);

            gl.glGetProgramiv(id, GL4.GL_INFO_LOG_LENGTH, infoLogLength);

            ByteBuffer infoLog = ByteBuffer.allocate(infoLogLength.get(0));

            gl.glGetProgramInfoLog(id, infoLogLength.get(0), infoLogLength, infoLog);

            String errorMessage = new String(infoLog.array(), Charset.forName("UTF-8"));

            throw new GLException(errorMessage);
        }

        return id;
    }

    /**
     * Parses a {@code .glsl} file and returns the shader source codes.
     * 
     * @param is is the input stream containing the shader code
     * @return the source code for the shaders
     * @throws IOException if the shader file is not found/failed to create streams/reader
     */
    private ShaderSource parse(InputStream is) throws IOException {
        ShaderSource source = new ShaderSource();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        ShaderType type = ShaderType.NONE;

        while ((line = reader.readLine()) != null) {
            if (line.contains("VERTEX_SHADER")) {
                type = ShaderType.VERTEX_SHADER;
            } else if (line.contains("FRAGMENT_SHADER")) {
                type = ShaderType.FRAGMENT_SHADER;
            } else {
                switch (type) {
                case VERTEX_SHADER:
                        source.vertexShaderSource[0] += line + "\r\n";
                        break;
                case FRAGMENT_SHADER:
                        source.fragmentShaderSource[0] += line + "\r\n";
                        break;
                default:
                        throw new GLException("Invalid shader file format.");
                }
            }
        }

        reader.close();

        return source;
    }
	
}
