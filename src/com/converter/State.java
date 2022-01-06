package com.converter;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    private String name;
    private boolean isStart;
    private boolean isFinal;

    private HashMap<String, ArrayList<State>> transitions;

    public State(String name) {
        this.name = name;
        isStart = false;
        isFinal = false;
        transitions = new HashMap<String, ArrayList<State>>();
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

    public HashMap<String, ArrayList<State>> getTransitions() {
        return transitions;
    }

    public void setTransitions(HashMap<String, ArrayList<State>> transitions) {
        this.transitions = transitions;
    }

    //Add functions to merge states
    //Add functions to add single transitions
    public void addSingleTransition(String symbol, State state) {
        ArrayList<State> temp = new ArrayList<>();
        temp.add(state);
        transitions.put(symbol, temp);
    }
    public void addTransition(String symbol, State state) {
        ArrayList <State> temp = transitions.get(symbol);
        if (temp == null) {
            temp = new ArrayList<>();
            temp.add(state);
            transitions.put(symbol, temp);
        }
    }
    //Add stuff here as needed!

//    @Override
//    public String toString() {
//        return "State{" +
//                "name='" + name + '\'' +
//                ", isStart=" + isStart +
//                ", isFinal=" + isFinal +
//                ", transitions=" + transitions +
//                '}';
//    }
}
