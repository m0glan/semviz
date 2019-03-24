package com.movlad.semviz.mvp;

public interface IParticipant {

    void handleMessage(String message, Object payload);

}
