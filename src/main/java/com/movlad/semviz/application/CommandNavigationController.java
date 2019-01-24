package com.movlad.semviz.application;

import java.util.ArrayList;
import java.util.List;

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

    public void enter(String command) {
        commands.add(commands.size() - 1, command);
    }

    public void up() {
        if (selectedIndex - 1 >= 0) {
            String prev = getSelection();

            selectedIndex--;

            changeSupport.firePropertyChange("CommandNavigationUp", prev, getSelection());
        }
    }

    public void down() {
        if (selectedIndex + 1 < commands.size()) {
            String prev = getSelection();

            selectedIndex++;

            changeSupport.firePropertyChange("CommandNavigationDown", prev, getSelection());
        }
    }

}
