package com.converter;

// This Class must implement two methods:
// 1. A converter which converts nfa-null to nfa
// 2. A converter which converts nfa to dfa

//Other helping methods and class members could be used

import java.util.*;

public class NFA {

    public NFA() {
    }

    private State merge_transitions(State s1, State s2) {
        HashMap<String, ArrayList<State>> s1_transitions = s1.getTransitions();
        HashMap<String, ArrayList<State>> s2_transitions = s2.getTransitions();

        for (String key : s2_transitions.keySet()) {
            ArrayList<State> transition_in_s1 = s1_transitions.get(key);
            if (transition_in_s1 == null) {
                s1_transitions.put(key, s2_transitions.get(key));
            } else {
                for (State x : s2_transitions.get(key)) {
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

        for (String key : nfa_from_file.keySet()) {
            State state = nfa_from_file.get(key);

            HashMap<String, ArrayList<State>> transitions = state.getTransitions();
            if (transitions.containsKey("@")) {
                for (State s : transitions.get("@")) {
                    State new_state = merge_transitions(state, s);
                    new_state.getTransitions().remove("@");
                    NFA.put(key, new_state);
                }

            } else {
                NFA.put(key, state);
            }

        }

        return NFA;
    }

    public HashMap<String, State> NFA_TO_DFA(HashMap<String, State> NFA) {

        HashMap<String, State> DFA = new HashMap<String, State>();
        Queue<String> pq = new PriorityQueue<>();
        State startState = null;
        for (String key : NFA.keySet()) {
            if (NFA.get(key).isStart())
                startState = NFA.get(key);
        }
        pq.add(startState.getName());
        while (!pq.isEmpty()) {
            String element = pq.remove();
            if (!DFA.containsKey(element)) {
                if (NFA.get(element) == null) {  // if it's a combined state, make it a new state object and add in DFA
                    NFA.put(element, new State(element));
                    // convert string to `char[]` array
                    char[] chars = element.toCharArray();

                    int count = 0;
                    // iterate over `char[]` array using enhanced for-loop
                    // if one of the characters is of a final state, then the combined state is final too
                    for (char ch : chars) {
                        if (NFA.get(Character.toString(ch)).isFinal()) {
                            NFA.get(element).setFinal(true);
                        }
                        count++;
                        if (count == 1) {

                            for (String tkey : NFA.get(Character.toString(ch)).getTransitions().keySet()) {
                                ArrayList<State> tempStates = new ArrayList<State>(NFA.get(Character.toString(ch)).getTransitions().get(tkey));
                                NFA.get(element).getTransitions().put(tkey, tempStates);

                            }

                        } else {
                            for (String tkey : NFA.get(Character.toString(ch)).getTransitions().keySet()) {
                                for (State s : NFA.get(Character.toString(ch)).getTransitions().get(tkey)) {
                                    if (NFA.get(element).getTransitions().get(tkey) == null) {
                                        ArrayList<State> tempStates = new ArrayList<State>(NFA.get(Character.toString(ch)).getTransitions().get(tkey));
                                        NFA.get(element).getTransitions().put(tkey, tempStates);
                                    } else {
                                        if (!NFA.get(element).getTransitions().get(tkey).contains(s)) {
                                            NFA.get(element).getTransitions().get(tkey).add(s);
                                        }
                                    }

                                }
                            }
                        }

                    }

                    for (String key : NFA.get(element).getTransitions().keySet()) {
                        ArrayList<State> transitions = NFA.get(element).getTransitions().get(key);
                        String appendedstates = "";
                        if (NFA.get(element).getTransitions().get(key).size() > 1) {
                            for (State transition : transitions) {
                                if (!appendedstates.contains(transition.getName())) {
                                    appendedstates = appendedstates.concat(transition.getName());
                                    char[] charArray = appendedstates.toCharArray();
                                    Arrays.sort(charArray);
                                    appendedstates = String.valueOf(charArray);
                                }

                            }
                            pq.add(appendedstates);
                        }


                    }


                }
                DFA.put(element, NFA.get(element));
                HashMap<String, ArrayList<State>> transitions = NFA.get(element).getTransitions();
                if (transitions.size() > 0) {  //check for null
                    for (String tkey : transitions.keySet()) {
                        String appendedStates = "";

                        for (State elementState : transitions.get(tkey)) {
                            if (!appendedStates.contains(elementState.getName())) {
                                appendedStates = appendedStates.concat(elementState.getName());
                                char[] charArray = appendedStates.toCharArray();
                                Arrays.sort(charArray);
                                appendedStates = String.valueOf(charArray);
                            }

                        }
                        pq.add(appendedStates);
                    }
                }

            }

        }


        for (String key : DFA.keySet()) {
            for (String tkey : DFA.get(key).getTransitions().keySet()) {
                ArrayList<State> transitions = DFA.get(key).getTransitions().get(tkey);
                String appendedstates = "";
                if (DFA.get(key).getTransitions().get(tkey).size() > 1) {
                    for (State transition : transitions) {
                        if (!appendedstates.contains(transition.getName())) {
                            appendedstates = appendedstates.concat(transition.getName());
                            char[] charArray = appendedstates.toCharArray();
                            Arrays.sort(charArray);
                            appendedstates = String.valueOf(charArray);
                        }

                    }
                    ArrayList<State> tempStates = new ArrayList<State>();
                    tempStates.add(DFA.get(appendedstates));
                    DFA.get(key).getTransitions().put(tkey, tempStates);

                }
            }


        }

        return DFA;
    }


}
