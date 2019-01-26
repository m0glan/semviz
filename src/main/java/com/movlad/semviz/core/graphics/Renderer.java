package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;

/**
 * Renders a scene with a camera on a canvas.
 */
public abstract class Renderer implements GLEventListener {

    private final Scene scene;
    private final Camera camera;

    private ShaderProgram program;

    private final List<VertexBufferObject> vbos;
    private final List<VertexArrayObject> vaos;

    public Renderer(Scene scene, Camera camera) {
        this.scene = scene;
        this.camera = camera;
        this.vbos = new ArrayList<>();
        this.vaos = new ArrayList<>();
    }

    @Override
    public final void init(GLAutoDrawable drawable) {
        GL3 gl = (GL3) drawable.getGL();

        gl.glEnable(GL3.GL_DEPTH_TEST);

        program = new ShaderProgram();

        try {
            InputStream vis = this.getClass().getClassLoader()
                    .getResourceAsStream("shaders/vshader.glsl");
            Shader vs = new Shader(vis, GL3.GL_VERTEX_SHADER);

            program.push(vs);

            InputStream fis = this.getClass().getClassLoader()
                    .getResourceAsStream("shaders/fshader.glsl");
            Shader fs = new Shader(fis, GL3.GL_FRAGMENT_SHADER);

            program.push(fs);
            program.link(gl);
            program.use(gl);
        } catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public final void display(GLAutoDrawable drawable) {
        GL3 gl = (GL3) drawable.getGL();

        program.use(gl);
        clear(gl);
        gl.glClearColor(0.027f, 0.184f, 0.372f, 1.0f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        scene.forEach(object -> {
            Geometry geometry = object.getGeometry();
            Buffer dataBuffer = FloatBuffer.wrap(geometry.getData());
            VertexBufferObject vbo = new VertexBufferObject(gl, dataBuffer,
                    dataBuffer.capacity() * Float.BYTES, GL3.GL_STATIC_DRAW);

            vbos.add(vbo);

            VertexArrayObject vao = new VertexArrayObject(gl);

            vaos.add(vao);
            vao.addBuffer(gl, vbo, geometry.getLayout());

            setObjectMatrixUniforms(gl, object);

            gl.glDrawArrays(geometry.getDrawingMode(), 0, geometry.getVertexCount());
        });
    }

    @Override
    public final void dispose(GLAutoDrawable drawable) {
        GL3 gl = (GL3) drawable.getGL();

        gl.glBindVertexArray(0);
        program.delete(gl);
    }

    private void clear(GL3 gl) {
        vbos.forEach(vbo -> {
            vbo.unbind(gl);
            vbo.delete(gl);
        });

        vbos.clear();

        vaos.forEach(vao -> {
            vao.unbind(gl);
            vao.delete(gl);
        });

        vaos.clear();
    }

    /**
     * Sets the matrix uniforms for the currently drawn renderable.
     *
     * @param renderable is the currently draw renderable
     */
    private void setObjectMatrixUniforms(GL3 gl, SceneObject object) {
        int modelUniformLocation = gl.glGetUniformLocation(program.getId(), "model");
        int viewUniformLocation = gl.glGetUniformLocation(program.getId(), "view");
        int projectionUniformLocation = gl.glGetUniformLocation(program.getId(), "projection");

        FloatBuffer modelBuffer = BufferUtils.createFloatBuffer(16);
        FloatBuffer viewBuffer = BufferUtils.createFloatBuffer(16);
        FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(16);

        object.getMatrixWorld().get(modelBuffer);
        camera.getMatrixWorld().get(viewBuffer);
        camera.getProjectionMatrix().get(projectionBuffer);

        gl.glUniformMatrix4fv(modelUniformLocation, 1, false, modelBuffer);
        gl.glUniformMatrix4fv(viewUniformLocation, 1, false, viewBuffer);
        gl.glUniformMatrix4fv(projectionUniformLocation, 1, false, projectionBuffer);
    }

}
