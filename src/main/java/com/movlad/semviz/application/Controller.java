/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.movlad.semviz.core.SemvizException;

import java.io.IOException;
import java.util.Observable;


public class Controller extends Observable {

    private static Controller instance = null;

    private Configuration config;

    public static Controller get() {
        if (instance == null) {
            instance = new Controller();
        }

        return instance;
    }

    public void load(String path) throws IOException, InvalidDirectoryException {
        try {
            config = new Configuration(path);

            setChanged();
            notifyObservers();
        } catch (IOException e) {
            throw e;
        } catch (InvalidDirectoryException e) {
            throw e;
        }
    }

}
