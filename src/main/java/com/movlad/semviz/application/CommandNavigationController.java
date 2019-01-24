package com.movlad.semviz.application;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller used for navigating through the command history.
 */
public class CommandNavigationController extends Controller {

    private final List<String> commands;
    private int selectedIndex;

    public CommandNavigationController() {
        commands = new ArrayList<>();

        commands.add("");

        selectedIndex = commands.size() - 1;
    }

    public String getSelection() {
        return commands.get(selectedIndex);
    }

    /**
     * Adds new command to history.
     *
     * @param command is the command to be added to history.
     */
    public void enter(String command) {
        commands.add(commands.size() - 1, command);
    }

    /**
     * Selects previous command.
     */
    public void up() {
        if (selectedIndex - 1 >= 0) {
            String prev = getSelection();

            selectedIndex--;

            changeSupport.firePropertyChange("CommandNavigationUp", prev, getSelection());
        }
    }

    /**
     * Selects a more recent command.
     */
    public void down() {
        if (selectedIndex + 1 < commands.size()) {
            String prev = getSelection();

            selectedIndex++;

            changeSupport.firePropertyChange("CommandNavigationDown", prev, getSelection());
        }
    }

}
