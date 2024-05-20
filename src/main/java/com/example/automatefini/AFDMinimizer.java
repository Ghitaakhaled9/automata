package com.example.automatefini;

import java.util.*;

public class AFDMinimizer {

    public static Automaton minimize(Automaton automaton) {
        Set<State> finalStates = new HashSet<>();
        Set<State> nonFinalStates = new HashSet<>();

        for (State state : automaton.getStates()) {
            if (state.isFinal()) {
                finalStates.add(state);
            } else {
                nonFinalStates.add(state);
            }
        }

        List<Set<State>> partitions = new ArrayList<>();
        if (!finalStates.isEmpty()) partitions.add(finalStates);
        if (!nonFinalStates.isEmpty()) partitions.add(nonFinalStates);

        boolean changed;
        do {
            changed = false;
            List<Set<State>> newPartitions = new ArrayList<>();

            // Step 3: Iterate over current partitions
            for (Set<State> partition : partitions) {
                // Map to group states by their transition behavior
                Map<String, Set<State>> groupedByTransitions = new HashMap<>();

                // Step 4: Group states based on their transition behavior on each terminal
                for (State state : partition) {
                    StringBuilder transitionKey = new StringBuilder();
                    for (char terminal : automaton.getTerminals()) {
                        State nextState = automaton.getNextState(state, terminal);
                        int partitionIndex = getPartitionIndex(partitions, nextState);
                        transitionKey.append(partitionIndex).append("-");
                    }
                    groupedByTransitions
                            .computeIfAbsent(transitionKey.toString(), k -> new HashSet<>())
                            .add(state);
                }

                // Step 5: Add groups with different transition behaviors to new partitions
                newPartitions.addAll(groupedByTransitions.values());
                if (groupedByTransitions.size() > 1) {
                    changed = true;
                }
            }

            partitions = newPartitions;

        } while (changed);

        // Step 6: Create new states, transitions, initial state, and final states for the minimized automaton
        Map<State, State> mergedStateMap = new HashMap<>();
        List<State> newStates = new ArrayList<>();
        List<Transition> newTransitions = new ArrayList<>();
        State newInitialState = null;
        Set<State> newFinalStates = new HashSet<>();

        int stateCounter = 0;
        for (Set<State> partition : partitions) {
            State representative = partition.iterator().next();
            State mergedState = new State(stateCounter++, containsFinalState(partition));
            newStates.add(mergedState);

            // Map original states to their merged state
            for (State state : partition) {
                mergedStateMap.put(state, mergedState);
            }

            // Determine initial state and final states of the minimized automaton
            if (automaton.getInState().equals(representative)) {
                newInitialState = mergedState;
            }
            if (containsFinalState(partition)) {
                newFinalStates.add(mergedState);
            }
        }

        // Step 7: Create new transitions based on merged states
        for (State oldState : automaton.getStates()) {
            State newState = mergedStateMap.get(oldState);
            for (char terminal : automaton.getTerminals()) {
                State oldNextState = automaton.getNextState(oldState, terminal);
                if (oldNextState != null) {
                    State newNextState = mergedStateMap.get(oldNextState);
                    newTransitions.add(new Transition(newState, terminal, newNextState));
                }
            }
        }

        // Step 8: Return the minimized automaton
        return new Automaton(
                automaton.getTerminals(),
                newStates.toArray(new State[0]),
                newTransitions.toArray(new Transition[0]),
                newInitialState,
                newFinalStates.toArray(new State[0])
        );
    }

    // Helper method to get the index of the partition containing a given state
    private static int getPartitionIndex(List<Set<State>> partitions, State state) {
        if (state == null) return -1;
        for (int i = 0; i < partitions.size(); i++) {
            if (partitions.get(i).contains(state)) {
                return i;
            }
        }
        return -1;
    }
    private static boolean containsFinalState(Set<State> stateSet) {
        for (State state : stateSet) {
            if (state.isFinal()) {
                return true;
            }
        }
        return false;
    }
}
