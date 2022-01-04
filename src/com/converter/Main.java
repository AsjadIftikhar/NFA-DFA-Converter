package com.converter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
	    // The Program Execution Starts from Here

        //Step 1: Read NDFSA-null from File:
        FileHandler fileHandler = new FileHandler("NFA");
        HashMap<String, State> allStates = fileHandler.readNFA();

        //Step 2: Convert NDFSA-null to NDFSA:
        NFA nfa = new NFA();
        HashMap<String, State> NFA = nfa.NFA_NULL_TO_NFA(allStates);


        //Step 3: Convert NDFSA to DFA

        //Step 4: Minimize DFA

        //Step 5: Run DFA to get Accept/Reject
    }
}
