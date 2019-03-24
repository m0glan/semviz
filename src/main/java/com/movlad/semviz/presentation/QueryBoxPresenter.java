package com.movlad.semviz.presentation;

import com.movlad.semviz.mvp.ObservableObject;
import com.movlad.semviz.core.sqm.SQM;
import com.movlad.semviz.mvp.IParticipant;
import com.movlad.semviz.mvp.Mediator;
import com.movlad.semviz.service.navigation.ILoadingDialogService;
import java.util.ArrayList;
import java.util.List;

public class QueryBoxPresenter extends ObservableObject
        implements IParticipant {

    private boolean isEnabled;

    private ILoadingDialogService loadingDialogService;

    private final List<String> queryHistory;
    private int selectedQueryIndex;

    private final Mediator mediator;

    public QueryBoxPresenter(Mediator mediator,
            ILoadingDialogService loadingDialogService) {
        this.queryHistory = new ArrayList<>();

        this.queryHistory.add("");

        this.selectedQueryIndex = queryHistory.size() - 1;
        this.mediator = mediator;

        this.mediator.addParticipant(this);

        this.isEnabled = false;
        this.loadingDialogService = loadingDialogService;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        boolean prev = isEnabled;

        this.isEnabled = isEnabled;

        raisePropertyChanged("IsEnabled", prev, isEnabled);
    }

    public String getSelectedQuery() {
        return queryHistory.get(selectedQueryIndex);
    }

    /**
     * Selects previous command.
     */
    public void previousQueryCommand() {
        if (selectedQueryIndex - 1 >= 0) {
            String prev = queryHistory.get(selectedQueryIndex);

            selectedQueryIndex--;

            raisePropertyChanged("SelectedCommandIndex", prev, selectedQueryIndex);
        }
    }

    /**
     * Selects a more recent command.
     */
    public void nextQueryCommand() {
        if (selectedQueryIndex + 1 < queryHistory.size()) {
            String prev = queryHistory.get(selectedQueryIndex);

            selectedQueryIndex++;

            raisePropertyChanged("SelectedCommandIndex", prev, selectedQueryIndex);
        }
    }

    /**
     * Adds new command to history.
     *
     * @param command is the command to be added to history.
     */
    public void executeQueryCommand(String command) {
        queryHistory.add(queryHistory.size() - 1, command);

        SQM.getInstance().exec(command);

        mediator.broadcastMessage(this, command, command);
    }

    @Override
    public void handleMessage(String message, Object payload) {

    }

}
