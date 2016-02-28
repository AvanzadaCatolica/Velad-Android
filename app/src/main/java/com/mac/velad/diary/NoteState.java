package com.mac.velad.diary;

import java.util.Arrays;

/**
 * Created by ruenzuo on 13/02/16.
 */
public enum NoteState {

    REGULAR ("Regular"), CONFESSABLE ("Confesable"), CONFESSED ("Confesado"), GUIDANCE ("Guia");

    private final String print;

    NoteState(String print) {
        this.print = print;
    }

    public static String[] states() {
        int length = NoteState.values().length;
        String[] states = new String[length];
        for (int i = 0; i < length; i++) {
            states[i] = NoteState.values()[i].toString();
        }
        return states;
    }

    public String toString() {
        return print;
    }

    public static NoteState fromPrint(String print) {
        return NoteState.values()[Arrays.asList(states()).indexOf(print)];
    }

}
