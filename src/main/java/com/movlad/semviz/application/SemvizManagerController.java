/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.movlad.semviz.application;

import com.movlad.semviz.core.SemvizException;
import com.movlad.semviz.core.math.geometry.PointCloud;
import com.movlad.semviz.core.semantic.SemvizQueryExecution;
import com.movlad.semviz.core.semantic.SemvizManager;
import java.io.IOException;
import java.util.Observable;
import org.apache.jena.query.QueryException;

public class SemvizManagerController extends Observable {
    
    private static SemvizManagerController instance = null;
    
    private SemvizManager svmgr;
    private SemvizQueryExecution semvizQueryExec;
    
    private SemvizManagerController() { 
        svmgr = SemvizManager.get(); 
        semvizQueryExec = null;
    }
    
    public SemvizQueryExecution getCloudSelection() { return semvizQueryExec; }
    
    public static SemvizManagerController get() {
        if (instance == null) {
            instance = new SemvizManagerController();
        }
        
        return instance;
    }
    
    public void load(String path) throws IOException, SemvizException {
        semvizQueryExec = null;
        
        try {
            svmgr.load(path);
        } catch (SemvizException | IOException e) {
            throw e;
        } finally {
            setChanged();
            notifyObservers();
        }
    }
    
    public void query(String queryString) throws SemvizException {
        try {
            semvizQueryExec = svmgr.query(queryString);
        } catch (SemvizException | QueryException e) {
            throw e;
        } finally {
            setChanged();
            notifyObservers();  
        }
    }
    
    public PointCloud retrieve(String cloudLocalName) throws SemvizException, IOException {
        return svmgr.retrieve(SemvizManager.NS + cloudLocalName, true);
    }
    
}
