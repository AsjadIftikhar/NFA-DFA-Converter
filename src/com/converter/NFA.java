package com.converter;

// This Class must implement two methods:
// 1. A converter which converts nfa-null to nfa
// 2. A converter which converts nfa to dfa

//Other helping methods and class members could be used

import java.util.ArrayList;
import java.util.HashMap;

public class NFA {

    private State merge_transitions(State s1, State s2) {
        HashMap<String, ArrayList<State>> s1_transitions = s1.getTransitions();
        HashMap<String, ArrayList<State>> s2_transitions = s2.getTransitions();

        for (String key: s2_transitions.keySet()) {
            ArrayList<State> transition_in_s1 = s1_transitions.get(key);
            if (transition_in_s1 == null) {
                s1_transitions.put(key, s2_transitions.get(key));
            }
            else {
                for (State x : s2_transitions.get(key)){
                    if (!transition_in_s1.contains(x))
                        transition_in_s1.add(x);
                }
                s1_transitions.replace(key, transition_in_s1);
            }
        }

        s1.setTransitions(s1_transitions);

        return s1;

    }

    // 1. A converter which converts nfa-null to nfa
    public HashMap<String, State> NFA_NULL_TO_NFA(HashMap<String, State> nfa_from_file) {
        HashMap<String, State> NFA = new HashMap<String, State>();

        for (String key: nfa_from_file.keySet()) {
            State state = nfa_from_file.get(key);

            HashMap<String, ArrayList<State>> transitions = state.getTransitions();
            if (transitions.containsKey("@")) {
                for (State s: transitions.get("@")) {
                    State new_state = merge_transitions(state, s);
                    NFA.put(key, new_state);
                }

            }
            else {
                NFA.put(key, state);
            }
            
        }

        return NFA;
    }
    // 2. A converter which converts nfa to dfa
    public HashMap<ArrayList<String>, State> NFA_TO_DFA(HashMap<String, State> NFA) {

        //ArrayList comparison
        //X = ['A', 'B'] == Y = ['A', 'B']
        return null;
    }


}
