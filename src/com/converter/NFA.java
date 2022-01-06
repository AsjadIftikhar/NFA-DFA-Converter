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

//    // 2. A converter which converts nfa to dfa
//    public HashMap<String, State> NFA_TO_DFA(HashMap<String, State> NFA) {
//        HashMap<String, State> DFA = new HashMap<>();
//
//        State startState = null;
//        for (String key : NFA.keySet()) {
//            if (NFA.get(key).isStart())
//                startState = NFA.get(key);
//        }
//
//        List<State> queue = new LinkedList<>();
//        queue.add(startState);
//
//        boolean first_row = true;
//
//        while (!queue.isEmpty()) {
//            State state_to_process = queue.remove(0);
//            if (DFA.get(state_to_process.getName()) == null) {
//                DFA.put(state_to_process.getName(), state_to_process);
//
//                if (first_row) {
//                    HashMap<String, ArrayList<State>> all_transitions = state_to_process.getTransitions();
//                    for (String symbol : all_transitions.keySet()) {
//                        ArrayList<State> transitions = all_transitions.get(symbol);
//
//                        StringBuilder new_state_name = new StringBuilder();
//                        boolean isStart = false;
//                        boolean isFinal = false;
//
//                        for (State old_state : transitions) {
//                            new_state_name.append(old_state.getName());
//                            if (old_state.isStart()) {
//                                isStart = true;
//                            }
//                            if (old_state.isFinal()) {
//                                isFinal = true;
//                            }
//                        }
//                        State s = new State(new_state_name.toString());
//                        s.setStart(isStart);
//                        s.setFinal(isFinal);
//
//                        HashMap<String, ArrayList<State>> new_transition = new HashMap<>();
//                        ArrayList<State> new_state_array = new ArrayList<>();
//                        new_state_array.add(s);
//                        new_transition.put(symbol, new_state_array);
//
//                        DFA.get(state_to_process.getName()).setTransitions(new_transition);
//
//                        queue.add(s);
//
//                        first_row = false;
//                    }
//
//                } else {
//                    for (int i = 0; i < state_to_process.getName().length(); i++) {
//                        State state_from_NFA = NFA.get(String.valueOf(state_to_process.getName().charAt(i)));
//
//                        HashMap<String, ArrayList<State>> all_transitions = state_from_NFA.getTransitions();
//                        for (String symbol : all_transitions.keySet()) {
//                            ArrayList<State> transitions = all_transitions.get(symbol);
//
//                            StringBuilder new_state_name = new StringBuilder();
//                            boolean isStart = false;
//                            boolean isFinal = false;
//
//                            for (State old_state : transitions) {
//                                new_state_name.append(old_state.getName());
//                                if (old_state.isStart()) {
//                                    isStart = true;
//                                }
//                                if (old_state.isFinal()) {
//                                    isFinal = true;
//                                }
//                            }
//                            State s = new State(new_state_name.toString());
//                            s.setStart(isStart);
//                            s.setFinal(isFinal);
//
//                            for (State value : DFA.values()) {
//                                if (Main.StringToArray(value.getName()).equals(Main.StringToArray(s.getName()))) {
//                                    s = value;
//                                }
//                            }
//
//                            HashMap<String, ArrayList<State>> new_transition = new HashMap<>(DFA.get(state_to_process.getName()).getTransitions());
//                            ArrayList<State> new_state_array = new ArrayList<>();
//                            new_state_array.add(s);
//                            new_transition.put(symbol, new_state_array);
//
//                            DFA.get(state_to_process.getName()).setTransitions(new_transition);
//
//                            queue.add(s);
//                        }
//
//                    }
//                }
//            }
//
//        }
//
//
//        return DFA;
//    }

    public HashMap<String, State> NFA_TO_DFA(HashMap<String, State> NFA) {

        HashMap<String, State> DFA = new HashMap<String, State>();
        Queue<String> pq = new PriorityQueue<>();
        State startState = null;
        for (String key: NFA.keySet()) {
            if(NFA.get(key).isStart())
                startState = NFA.get(key);
        }
        pq.add(startState.getName());
        while(!pq.isEmpty()) {
            String element = pq.remove();
            if(!DFA.containsKey(element)){
                if(NFA.get(element) == null){  // if it's a combined state, make it a new state object and add in DFA
                    NFA.put(element, new State(element));
                    // convert string to `char[]` array
                    char[] chars = element.toCharArray();

                    int count = 0;
                    // iterate over `char[]` array using enhanced for-loop
                    // if one of the characters is of a final state, then the combined state is final too
                    for (char ch: chars) {
                        if(NFA.get(Character.toString(ch)).isFinal()){
                            NFA.get(element).setFinal(true);
                        }
                        count++;
                        if(count == 1) {

                            for(String tkey: NFA.get(Character.toString(ch)).getTransitions().keySet()){
                                ArrayList<State> tempStates = new ArrayList<State>();
                                tempStates.addAll(NFA.get(Character.toString(ch)).getTransitions().get(tkey));
                                NFA.get(element).getTransitions().put(tkey,tempStates);

                            }

                        }
                        else{
                            for(String tkey: NFA.get(Character.toString(ch)).getTransitions().keySet()){
                                for(State s: NFA.get(Character.toString(ch)).getTransitions().get(tkey)) {
                                    if(NFA.get(element).getTransitions().get(tkey) == null)
                                    {
                                        ArrayList<State> tempStates = new ArrayList<State>();
                                        tempStates.addAll(NFA.get(Character.toString(ch)).getTransitions().get(tkey));
                                        NFA.get(element).getTransitions().put(tkey,tempStates);
                                    }
                                    else {
                                        if (!NFA.get(element).getTransitions().get(tkey).contains(s)) {
                                            NFA.get(element).getTransitions().get(tkey).add(s);
                                        }
                                    }

                                }
                            }
                        }

                    }

                    for (String key: NFA.get(element).getTransitions().keySet()) {
                        ArrayList<State> transitions = NFA.get(element).getTransitions().get(key);
                        String appendedstates = "";
                        if(NFA.get(element).getTransitions().get(key).size() > 1) {
                            for (int i = 0; i < transitions.size(); i++) {
                                if(!appendedstates.contains(transitions.get(i).getName())) {
                                    appendedstates = appendedstates.concat(transitions.get(i).getName());
                                    char charArray[] = appendedstates.toCharArray();
                                    Arrays.sort(charArray);
                                    appendedstates = String.valueOf(charArray);
                                }

                            }
                            pq.add(appendedstates);
                        }


                    }


                }
                DFA.put(element,NFA.get(element));
                HashMap<String, ArrayList<State>> transitions = NFA.get(element).getTransitions();
                if(transitions.size() > 0) {  //check for null
                    for (String tkey : transitions.keySet()) {
                        String appendedStates = "";

                        for (State elementState : transitions.get(tkey)) {
                            if(!appendedStates.contains(elementState.getName())) {
                                appendedStates = appendedStates.concat(elementState.getName());
                                char charArray[] = appendedStates.toCharArray();
                                Arrays.sort(charArray);
                                appendedStates = String.valueOf(charArray);
                            }

                        }
                        pq.add(appendedStates);
                    }
                }

            }

        }


        for (String key: DFA.keySet()) {
            for (String tkey: DFA.get(key).getTransitions().keySet()) {
                ArrayList<State> transitions = DFA.get(key).getTransitions().get(tkey);
                String appendedstates = "";
                if (DFA.get(key).getTransitions().get(tkey).size() > 1) {
                    for (int i = 0; i < transitions.size(); i++) {
                        if(!appendedstates.contains(transitions.get(i).getName())) {
                            appendedstates = appendedstates.concat(transitions.get(i).getName());
                            char charArray[] = appendedstates.toCharArray();
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
