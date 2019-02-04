package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;

/**
 * Renders a scene with a camera on a canvas.
 */
public abstract class Renderer implements GLEventListener {

    private volatile Scene scene;
    private Camera camera;

    private ShaderProgram program;

    private final IntBuffer VAO;
    private IntBuffer vaos;
    private IntBuffer vbos;

    public Renderer() {
        this.VAO = IntBuffer.allocate(1);
    }

    public final void setScene(Scene scene) {
        this.scene = scene;
    }

    public final void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public final void init(GLAutoDrawable drawable) {
        GL3 gl = (GL3) drawable.getGL();

        gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glGenVertexArrays(1, VAO);

        program = new ShaderProgram();

        try {
            InputStream vis = this.getClass().getClassLoader()
                    .getResourceAsStream("shaders/vshader.glsl");
            Shader vs = new Shader(vis, GL3.GL_VERTEX_SHADER);

            program.add(vs);

            InputStream fis = this.getClass().getClassLoader()
                    .getResourceAsStream("shaders/fshader.glsl");
            Shader fs = new Shader(fis, GL3.GL_FRAGMENT_SHADER);

            program.add(fs);
            program.link(gl);
        } catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public final void display(GLAutoDrawable drawable) {
        GL3 gl = (GL3) drawable.getGL();

        gl.glUseProgram(program.getId());
        gl.glClearColor(0.027f, 0.184f, 0.372f, 1.0f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        /**
         * Generates vertex buffer and corresponding vertex array for each
         * object of the scene.
         *
         * @param gl is the gl context
         */
        vbos = BufferUtils.createIntBuffer(scene.size());

        gl.glGenBuffers(scene.size(), vbos);

        vaos = BufferUtils.createIntBuffer(scene.size());

        gl.glGenVertexArrays(scene.size(), vaos);

        for (int i = 0; i < scene.size(); i++) {
            SceneObject object = scene.get(i);
            Geometry geometry = object.getGeometry();

            GLUtils.geometryToGL(gl, geometry, vbos.get(i), vaos.get(i));

            if (object.isVisible()) {
                gl.glBindVertexArray(vaos.get(i));

                GLUtils.setUniformMatrix4fv(gl, program.getId(), "model", object.getMatrixWorld());
                GLUtils.setUniformMatrix4fv(gl, program.getId(), "view", camera.getMatrixWorld());
                GLUtils.setUniformMatrix4fv(gl, program.getId(), "projection", camera.getProjectionMatrix());

                gl.glDrawArrays(geometry.getDrawingMode(), 0, geometry.vertexCount());
            }
        }

        freeGLData(gl);
        gl.glBindVertexArray(VAO.get(0));
        gl.glFlush();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = (GL3) drawable.getGL();

        freeGLData(gl);
        gl.glDeleteProgram(program.getId());
    }

    private void freeGLData(GL3 gl) {
        if (vaos.capacity() > 0) {
            gl.glDeleteVertexArrays(vaos.capacity(), vaos);
        }

        if (vbos.capacity() > 0) {
            gl.glDeleteBuffers(vbos.capacity(), vbos);
        }
    }

}
