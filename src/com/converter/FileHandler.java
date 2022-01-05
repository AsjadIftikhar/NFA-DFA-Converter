package com.converter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileHandler {

    HashMap<String, State> allStates;
    File file;
    BufferedReader br;
    String delimiter;

    // Add a suitable Constructor
    public FileHandler(String filename) {
        allStates = new HashMap<String, State>();
        file = new File(filename + ".txt");

        delimiter = "\\s+";
    }


    // Add Two methods:
    // 1. To Read a NDFSA from File in txt format

    public HashMap<String, State> readNFA() throws IOException {
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            int LineNumber = 0;

            while ((line = br.readLine()) != null) {

                LineNumber++;

                //Read States:
                if (LineNumber == 1) {
                    String[] values = line.split(delimiter);
                    for (String value : values) {
                        allStates.put(value.trim(), new State(value.trim()));
                    }
                    // first letter is start state
                    allStates.get(values[0]).setStart(true);
                }
                //Read Final States
                else if (LineNumber == 2) {
                    String[] values = line.split(delimiter);
                    for (String value : values) {
                        allStates.get(value.trim()).setFinal(true);
                    }
                }
                // skip for now
                else if (LineNumber == 3) {
                    continue;
                }
                // Read Productions
                else {
                    String[] values = line.split(delimiter);
                    String currentState = values[0].trim();
                    String nextState = values[1].trim();
                    String symbol = values[2].trim();

                    if (allStates.get(currentState).getTransitions().containsKey(symbol)) {
                        allStates.get(currentState.trim()).getTransitions().get(symbol.trim()).add(allStates.get(nextState.trim()));

                    } else {
                        ArrayList<State> tempStates = new ArrayList<State>();
                        tempStates.add(allStates.get(nextState));
                        allStates.get(currentState).getTransitions().put(symbol, tempStates);
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        br.close();
        return allStates;
    }

    // 2. Write a DFA transition table back to another File in txt format
    public void writeDFA(HashMap<String, State> DFA) throws IOException {
       FileWriter fileWriter = new FileWriter("DFA.txt");
       fileWriter.write("DFA Transitions\n");
        for (State state: DFA.values()) {
            HashMap<String, ArrayList<State>> transitions = state.getTransitions();
            for (String symbol: transitions.keySet()) {
                fileWriter.write(state.getName() + "  " + transitions.get(symbol).get(0).getName() + "  " + symbol + "\n");
            }
        }

        fileWriter.close();


    }

}