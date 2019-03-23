package com.movlad.semviz.patterns.mediator;

public interface IParticipant {

    void handleMessage(String message, Object payload);

}
