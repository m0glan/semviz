package com.movlad.semviz.presentation;

import com.movlad.semviz.mvp.ObservableObject;
import com.movlad.semviz.service.navigation.IFileChooserService;

public class MainFramePresenter extends ObservableObject {

    private IFileChooserService fileChooserService;

    public MainFramePresenter(IFileChooserService fileChooserService) {
        this.fileChooserService = fileChooserService;
    }

}
