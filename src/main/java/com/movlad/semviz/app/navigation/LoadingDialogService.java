package com.movlad.semviz.app.navigation;

import com.movlad.semviz.service.navigation.ILoadingDialogService;
import com.movlad.semviz.view.LoadingDialog;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

public class LoadingDialogService implements ILoadingDialogService {

    @Override
    public void showLoadingDialog(Window parent, Runnable runnable) {
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                runnable.run();

                return null;
            }
        };

        final JDialog dialog = new LoadingDialog(parent);

        swingWorker.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if (evt.getPropertyName().equals("state")) {
                if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                    dialog.dispose();
                }
            }
        });

        swingWorker.execute();

        dialog.setModal(true);
        dialog.setVisible(true);
    }

}
