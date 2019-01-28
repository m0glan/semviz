package com.movlad.semviz.core.graphics;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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

    private IntBuffer vaos;

    public Renderer(Scene scene, Camera camera) {
        this.scene = scene;
        this.camera = camera;
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
        } catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public final void display(GLAutoDrawable drawable) {
        GL3 gl = (GL3) drawable.getGL();

        generateVertexArrays(gl);

        program.use(gl);

        gl.glClearColor(0.027f, 0.184f, 0.372f, 1.0f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        for (int i = 0; i < scene.size(); i++) {
            draw(gl, scene.get(i), vaos.get(i));
        }
    }

    private void draw(GL3 gl, SceneObject object, int vao) {
        gl.glBindVertexArray(vao);

        Geometry geometry = object.getGeometry();

        setObjectMatrixUniforms(gl, object);
        gl.glDrawArrays(geometry.getDrawingMode(), 0, geometry.getVertexCount());
    }

    private void generateVertexArrays(GL3 gl) {
        if (scene.size() > 0) {
            IntBuffer vbos = BufferUtils.createIntBuffer(scene.size());

            gl.glGenBuffers(scene.size(), vbos);

            vaos = BufferUtils.createIntBuffer(scene.size());

            gl.glGenVertexArrays(scene.size(), vaos);

            for (int i = 0; i < scene.size(); i++) {
                Geometry geometry = scene.get(i).getGeometry();

                gl.glBindVertexArray(vaos.get(i));
                gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbos.get(i));
                gl.glBufferData(GL3.GL_ARRAY_BUFFER, geometry.getDataSize(), geometry.getData(),
                        GL3.GL_STATIC_DRAW);

                BufferLayout layout = geometry.getLayout();
                int offset = 0;

                for (int j = 0; j < layout.size(); j++) {
                    BufferAttribute attribute = layout.get(j);

                    gl.glEnableVertexAttribArray(j);
                    gl.glVertexAttribPointer(j, attribute.getSize(), attribute.getType(),
                            attribute.getNormalized(), layout.getStride(), offset);

                    offset += attribute.sizeInBytes();
                }
            }
        }
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

    private void clear(GL3 gl) {
        gl.glDeleteVertexArrays(vaos.capacity(), vaos);
    }

    @Override
    public final void dispose(GLAutoDrawable drawable) {
        GL3 gl = (GL3) drawable.getGL();

        clear(gl);
    }

}
