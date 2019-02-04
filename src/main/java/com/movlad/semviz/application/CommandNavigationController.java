package com.movlad.semviz.application;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller used for navigating through the command history.
 */
public final class CommandNavigationController extends Controller {

    private final List<String> commands;
    private int selectedCommandIndex;

    public CommandNavigationController() {
        commands = new ArrayList<>();

        commands.add("");

        selectedCommandIndex = commands.size() - 1;
    }

    /**
     * Adds new command to history.
     *
     * @param command is the command to be added to history.
     */
    public void enter(String command) {
        commands.add(commands.size() - 1, command);

        changeSupport.firePropertyChange("CommandLaunch", null, command);
    }

    /**
     * Selects previous command.
     */
    public void up() {
        if (selectedCommandIndex - 1 >= 0) {
            String prev = commands.get(selectedCommandIndex);

            selectedCommandIndex--;

            changeSupport.firePropertyChange("CommandNavigationUp", prev,
                    commands.get(selectedCommandIndex));
        }
    }

    /**
     * Selects a more recent command.
     */
    public void down() {
        if (selectedCommandIndex + 1 < commands.size()) {
            String prev = commands.get(selectedCommandIndex);

            selectedCommandIndex++;

            changeSupport.firePropertyChange("CommandNavigationDown", prev,
                    commands.get(selectedCommandIndex));
        }
    }

}
