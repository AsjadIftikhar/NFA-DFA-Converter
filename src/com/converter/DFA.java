package com.converter;

import java.util.*;

// This Class must implement two methods:
// 1. A DFA minimizer: to minimize a DFA
// 2. A DFA executor: to run a sample string on the DFA
public class DFA {

    public DFA() {
    }

    private boolean is_in_same_group(ArrayList<ArrayList<State>> all_groups, State state1, State state2) {
        for (ArrayList<State> states : all_groups) {
            if (states.contains(state1) ^ states.contains(state2)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private ArrayList<ArrayList<State>> minimize(ArrayList<ArrayList<State>> all_groups) {

        ArrayList<ArrayList<State>> next_round = new ArrayList<>();
        for (ArrayList<State> group : all_groups) {
            if (group.size() > 1) {
                int first_state = 0;
                ArrayList<State> new_group_1 = new ArrayList<>();
                ArrayList<State> new_group_2 = new ArrayList<>();

                boolean isEquivalent = false;
                for (int i = 1; i < group.size(); i++) {
                    Set<String> first_set = group.get(first_state).getTransitions().keySet();
                    Set<String> second_set = group.get(i).getTransitions().keySet();
                    Set<String> intersection = new HashSet<>(first_set);
                    intersection.retainAll(second_set);

                    isEquivalent = false;
                    for (String element : intersection) {
                        State state_1 = group.get(first_state).getTransitions().get(element).get(0);
                        State state_2 = group.get(i).getTransitions().get(element).get(0);

                        isEquivalent = is_in_same_group(all_groups, state_1, state_2);

                    }

                    if (!new_group_1.contains(group.get(first_state))) {
                        new_group_1.add(group.get(first_state));
                    }
                    if (isEquivalent) {
                        new_group_1.add(group.get(i));
                    } else {
                        new_group_2.add(group.get(i));
                    }

                }
                if (!new_group_1.isEmpty()) {
                    next_round.add(new_group_1);
                }
                if (!new_group_2.isEmpty()) {
                    next_round.add(new_group_2);
                }
            } else {
                next_round.add(group);
            }
        }
        if (all_groups.size() == next_round.size()) {
            return next_round;
        }
        return minimize(next_round);

    }

    private HashMap<String, State> remove_unreachable_states(HashMap<String, State> converted_dfa) {
        //Step 1: Find start state
        State start_state = null;
        for (String key : converted_dfa.keySet()) {
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
            for (String key : unreachable_states) {
                converted_dfa.remove(key);
            }
        }


        return converted_dfa;
    }

    // 1. A DFA minimizer: to minimize a DFA
    public HashMap<String, State> MINIMIZE_DFA(HashMap<String, State> converted_dfa) {

        //Step 1: remove unreachable states using BFS algorithm
        HashMap<String, State> DFA = remove_unreachable_states(converted_dfa);

        //Step 2: Make groups and reduce states
        //Step 2.1: Make two groups depending on final flag
        ArrayList<State> non_final_states = new ArrayList<>();
        ArrayList<State> final_states = new ArrayList<>();

        for (String key : DFA.keySet()) {
            if (DFA.get(key).isFinal()) {
                final_states.add(DFA.get(key));
            } else {
                non_final_states.add(DFA.get(key));
            }
        }

        //Step 2.2 add both groups in all groups
        ArrayList<ArrayList<State>> all_groups = new ArrayList<>();
        all_groups.add(non_final_states);
        all_groups.add(final_states);

        //Step 2.3: Minimize all groups
        all_groups = minimize(all_groups);

        //Step 3: make a DFA from groups
        HashMap<String, State> minimized_DFA = new HashMap<>();
        for (ArrayList<State> group: all_groups) {
            StringBuilder name = new StringBuilder();
            boolean isStart = false;
            boolean isFinal = false;

            for (State state: group) {
                name.append(state.getName());
                if (state.isStart()){
                    isStart = true;
                }
                if (state.isFinal()) {
                    isFinal = true;
                }
            }
            State s = new State(name.toString());
            s.setStart(isStart);
            s.setFinal(isFinal);
            minimized_DFA.put(name.toString(), s);
        }

        //Step 4: Populate transitions for DFA
        for (String key_name: minimized_DFA.keySet()) {
            ArrayList<String> tokens = Main.StringToArray(key_name);
            for (String state: tokens) {
                HashMap<String, ArrayList<State>> old_transitions = DFA.get(state).getTransitions();
                for (String key_transition: old_transitions.keySet()) {
                    State old_state = old_transitions.get(key_transition).get(0);
                    for (String new_name: minimized_DFA.keySet()) {
                        if (new_name.contains(old_state.getName())) {

                            minimized_DFA.get(key_name).addSingleTransition(key_transition, minimized_DFA.get(new_name));
                        }
                    }
                }
            }
        }

        return minimized_DFA;
    }


    // 2. A DFA executor: to run a sample string on the DFA
    public boolean EXECUTE_ON_DFA(HashMap<String, State> DFA, String input_tape) {

        //Step 1: Find start state
        State current_state = null;
        for (String key : DFA.keySet()) {
            if (DFA.get(key).isStart()) {
                current_state = DFA.get(key);
                break;
            }
        }

        //Step 2: Find Final States
        ArrayList<State> final_states = new ArrayList<>();
        for (String key : DFA.keySet()) {
            if (DFA.get(key).isFinal()) {
                final_states.add(DFA.get(key));
            }
        }

        //Step 3: Execute input string on DFA
        for (int i = 0; i < input_tape.length(); i++) {
            assert current_state != null;
            String input_char = String.valueOf(input_tape.charAt(i));
            if (current_state.getTransitions().containsKey(input_char)) {
                System.out.print(current_state.getName() + " "+ input_char  + " -> ");
                current_state = current_state.getTransitions().get(input_char).get(0);
            }
            else {
                return false;
            }
        }
        System.out.println(current_state.getName());
        return final_states.contains(current_state);
    }
}