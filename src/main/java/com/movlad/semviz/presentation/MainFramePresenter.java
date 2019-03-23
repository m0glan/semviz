package com.movlad.semviz.presentation;

import com.movlad.semviz.service.navigation.IFileChooserService;

public class MainFramePresenter extends PresenterBase {

    private IFileChooserService fileChooserService;

    public MainFramePresenter(IFileChooserService fileChooserService) {
        this.fileChooserService = fileChooserService;
    }

}
