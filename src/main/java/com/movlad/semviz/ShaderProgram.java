package com.movlad.semviz;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLException;

public class ShaderProgram {

	private int id;
	private GL4 gl;
	
	public ShaderProgram(GL4 gl, String[] vertexShaderSource, String[] fragmentShaderSource) 
			throws GLException {
		this.gl = gl;
		this.id = compileProgram(vertexShaderSource, fragmentShaderSource);
	}
	
	public void use() {
		gl.glUseProgram(id);
	}
	
	private int compileShader(int type, String[] source) throws GLException {
		int id = gl.glCreateShader(type);
		
		gl.glShaderSource(id, 1, source, (int[]) null, 0);
		gl.glCompileShader(id);
		
		IntBuffer success = IntBuffer.allocate(1);
		
		gl.glGetShaderiv(id, GL4.GL_COMPILE_STATUS, success);
		
		if (success.get(0) != GL4.GL_TRUE) {
			IntBuffer infoLogLength = IntBuffer.allocate(1);
			
			gl.glGetShaderiv(id, GL4.GL_INFO_LOG_LENGTH, infoLogLength);
			
			ByteBuffer infoLog = ByteBuffer.allocate(infoLogLength.get(0));
			
			gl.glGetShaderInfoLog(id, infoLogLength.get(0), infoLogLength, infoLog);
			
			String errorMessage = new String(infoLog.array(), Charset.forName("UTF-8"));
			
			throw new GLException(errorMessage);
		}
		
		return id;
	}
	
	private int compileProgram(String[] vertexShaderSource, String[] fragmentShaderSource) 
			throws GLException {
		int vertexShader = compileShader(GL4.GL_VERTEX_SHADER, vertexShaderSource);
		int fragmentShader = compileShader(GL4.GL_FRAGMENT_SHADER, fragmentShaderSource);
		int id = gl.glCreateProgram();
		
		gl.glAttachShader(id, vertexShader);
		gl.glAttachShader(id, fragmentShader);
		gl.glLinkProgram(id);
		gl.glDeleteShader(vertexShader);
		gl.glDeleteShader(fragmentShader);
		
		IntBuffer success = IntBuffer.allocate(1);
		
		gl.glGetProgramiv(id, GL4.GL_COMPILE_STATUS, success);
		
		if (success.get(0) != GL4.GL_TRUE) {
			IntBuffer infoLogLength = IntBuffer.allocate(1);
			
			gl.glGetProgramiv(id, GL4.GL_INFO_LOG_LENGTH, infoLogLength);
			
			ByteBuffer infoLog = ByteBuffer.allocate(infoLogLength.get(0));
			
			gl.glGetProgramInfoLog(id, infoLogLength.get(0), infoLogLength, infoLog);
			
			String errorMessage = new String(infoLog.array(), Charset.forName("UTF-8"));
			
			throw new GLException(errorMessage);
		}
		
		return id;
	}
	
}
