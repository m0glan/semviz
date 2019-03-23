package com.movlad.semviz.patterns.mediator;

import java.util.ArrayList;
import java.util.List;

public class Mediator {

    private final List<IParticipant> participants;

    public Mediator() {
        participants = new ArrayList<>();
    }

    public void addParticipant(IParticipant participant) {
        participants.add(participant);
    }

    public void removeParticipant(IParticipant participant) {
        participants.remove(participant);
    }

    public void broadcastMessage(IParticipant sender, String message, Object payload) {
        participants.forEach(participant -> {
            if (participant != sender) {
                participant.handleMessage(message, payload);
            }
        });
    }

}
