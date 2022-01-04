package com.converter;

import java.util.HashMap;

public class State {
    private String name;
    private boolean isStart;
    private boolean isFinal;

    private HashMap<String, State> transitions;

    public State(String name) {
        this.name = name;
        isStart = false;
        isFinal = false;
        transitions = new HashMap<String, State>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public HashMap<String, State> getTransitions() {
        return transitions;
    }

    public void setTransitions(HashMap<String, State> transitions) {
        this.transitions = transitions;
    }

    //Add functions to merge states
    //Add functions to add single transitions
    //Add stuff here as needed!

    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                ", isStart=" + isStart +
                ", isFinal=" + isFinal +
                ", transitions=" + transitions +
                '}';
    }
}
