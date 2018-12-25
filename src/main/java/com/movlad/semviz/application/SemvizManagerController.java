/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.movlad.semviz.core.SemvizException;
import com.movlad.semviz.core.math.geometry.PointCloud;
import com.movlad.semviz.core.semantic.CloudSelection;
import com.movlad.semviz.core.semantic.SemvizManager;
import java.io.IOException;
import java.util.Observable;

public class SemvizManagerController extends Observable {
    
    private static SemvizManagerController instance = null;
    
    private SemvizManager svmgr;
    private CloudSelection cloudSelection;
    private PointCloud loadedCloud;
    
    private SemvizManagerController() { 
        svmgr = SemvizManager.get(); 
        cloudSelection = null;
    }
    
    public CloudSelection getCloudSelection() { return cloudSelection; }
    
    public PointCloud getLoadedCloud() { return loadedCloud; }
    
    public static SemvizManagerController get() {
        if (instance == null) {
            instance = new SemvizManagerController();
        }
        
        return instance;
    }
    
    public void load(String path) throws IOException, SemvizException {
        svmgr.load(path);
        
        setChanged();
        notifyObservers();
    }
    
    public void query(String queryString) throws SemvizException {
        cloudSelection = svmgr.query(queryString);
        
        setChanged();
        notifyObservers();
    }
    
    public void retrieve(String cloudURI) throws SemvizException, IOException {
        loadedCloud = svmgr.retrieve(cloudURI, true);
        
        setChanged();
        notifyObservers();
    }
    
}
