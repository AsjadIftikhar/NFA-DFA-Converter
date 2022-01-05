package com.converter;

import java.util.*;

// This Class must implement two methods:
// 1. A DFA minimizer: to minimize a DFA
// 2. A DFA executor: to run a sample string on the DFA
public class DFA {

    private HashMap<String, State> remove_unreachable_states(HashMap<String, State> converted_dfa) {
        //Step 1: Find start state
        State start_state = null;
        for (String key: converted_dfa.keySet()) {
            if (converted_dfa.get(key).isStart()) {
                start_state = converted_dfa.get(key);
                break;
            }
        }

        //Step 2: We assume all states are not reachable for now
        ArrayList<String> unreachable_states = new ArrayList<>(converted_dfa.keySet());

        //Step 3: Initialize a queue with transitions from start state
        List<State> queue = new LinkedList<>();
        queue.add(start_state);

        //Step4: Test all states to see if they are reachable
        while (!queue.isEmpty()) {
            State state_to_process = queue.remove(0);

            for (String k : state_to_process.getTransitions().keySet()) {
                if (unreachable_states.contains(state_to_process.getTransitions().get(k).get(0).getName())) {
                    queue.add(state_to_process.getTransitions().get(k).get(0));

                }
            }
            //Once reached we remove the state from unreachable
            unreachable_states.remove(state_to_process.getName());
        }
        //remove unreachable states from dfa
        if (unreachable_states.size() >= 1) {
            for (String key: unreachable_states) {
                converted_dfa.remove(key);
            }
        }


        return converted_dfa;
    }
    public DFA() {}

    // 1. A DFA minimizer: to minimize a DFA
    public HashMap<String, State> MINIMIZE_DFA(HashMap<String, State> converted_dfa) {

        HashMap<String, State> DFA = remove_unreachable_states(converted_dfa);

        return null;
    }



    // 2. A DFA executor: to run a sample string on the DFA
    public boolean EXECUTE_ON_DFA(HashMap<ArrayList<String>, State> DFA) {
        return false;
    }
}