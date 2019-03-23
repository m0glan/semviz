package com.movlad.semviz.service.navigation;

import java.awt.Window;

public interface ILoadingDialogService {

    void showLoadingDialog(Window parent, Runnable task);

}
