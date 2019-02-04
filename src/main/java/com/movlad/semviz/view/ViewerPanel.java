package com.movlad.semviz.view;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.Animator;
import com.movlad.semviz.application.SQMController;
import com.movlad.semviz.application.SceneController;
import com.movlad.semviz.core.graphics.Controls;
import com.movlad.semviz.core.graphics.OrbitControls;
import com.movlad.semviz.core.graphics.OrthographicCamera;
import com.movlad.semviz.core.graphics.Renderer;
import com.movlad.semviz.core.graphics.Scene;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.joml.Vector3f;

/**
 * Interface component on which a scene is drawn.
 */
public final class ViewerPanel extends GLJPanel implements PropertyChangeListener {

    private final OrthographicCamera camera;
    private final Controls controls;
    private final Renderer renderer;
    private final Animator animator;

    /**
     * Constructor.
     *
     * @param d is the dimension of the component
     * @param sceneController is the controller containing the scene to be
     * observed
     */
    public ViewerPanel(Dimension d, SceneController sceneController) {
        super(new GLCapabilities(GLProfile.get(GLProfile.GL3)));
        setSize(d);

        sceneController.register(this);
        SQMController.getInstance().register(this);

        Scene scene = new Scene();

        camera = new OrthographicCamera(-getWidth() / 2, getWidth() / 2, -getHeight() / 2, getHeight() / 2,
                0.1f, 1000.0f);

        camera.translate(new Vector3f(-10.0f, -10.0f, 5.0f));
        camera.setTarget(new Vector3f(0.0f, 0.0f, 0.0f));
        camera.zoom(50);

        controls = new OrbitControls(camera);

        controls.setZoomSpeed(3.0f);

        addMouseListener(controls);
        addMouseMotionListener(controls);
        addMouseWheelListener(controls);

        renderer = new Renderer() {

            @Override
            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                drawable.getGL().glViewport(x, y, width, height);
                ((OrthographicCamera) camera).setFrustum(-width / 2, width / 2, -height / 2, height / 2);
                camera.updateProjectionMatrix();
            }

        };

        renderer.setCamera(camera);
        renderer.setScene(scene);
        addGLEventListener(renderer);

        animator = new Animator(this);

        animator.start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("SQMExecutionStarted")) {
            animator.stop();
        }

        if (evt.getPropertyName().contains("Scene")) {
            renderer.setScene((Scene) evt.getNewValue());
            animator.start();
        }
    }

}
