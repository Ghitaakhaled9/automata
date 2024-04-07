package com.example.automatefini;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AFDMinimizer {

    public static Automaton minimize(Automaton automaton) {
        // Clone the automaton to preserve the original
        Automaton minimizedAutomaton = cloneAutomaton(automaton);

        // Step 1: Split states into two sets - final and non-final
        Set<State> finalStates = new HashSet<>();
        Set<State> nonFinalStates = new HashSet<>();
        for (State state : minimizedAutomaton.getStates()) {
            if (state.isFinal()) {
                finalStates.add(state);
            } else {
                nonFinalStates.add(state);
            }
        }

        // Step 2: Continue splitting states until no further splits are possible
        boolean changed;
        do {
            changed = false;
            Set<Set<State>> newPartition = new HashSet<>();

            // Add final and non-final states to the partition
            newPartition.add(finalStates);
            newPartition.add(nonFinalStates);

            // Iterate over each state pair in the current partition
            for (Set<State> stateSet : newPartition) {
                Set<Set<State>> split = splitStateSet(stateSet, minimizedAutomaton);
                if (split.size() > 1) {
                    newPartition.remove(stateSet);
                    newPartition.addAll(split);
                    changed = true;
                    break;
                }
            }

            if (changed) {
                // Update final and non-final states based on the new partition
                finalStates.clear();
                nonFinalStates.clear();
                for (Set<State> stateSet : newPartition) {
                    if (containsFinalState(stateSet)) {
                        finalStates.addAll(stateSet);
                    } else {
                        nonFinalStates.addAll(stateSet);
                    }
                }
            }
        } while (changed);

        // Create the minimized automaton
        State initialState = getMergedState(minimizedAutomaton.getInState(), finalStates);
        Set<State> mergedFinalStates = new HashSet<>();
        for (State finalState : finalStates) {
            mergedFinalStates.add(getMergedState(finalState, finalStates));
        }

        List<State> mergedStates = new ArrayList<>();
        mergedStates.add(initialState);
        mergedStates.addAll(mergedFinalStates);
        mergedStates.addAll(getMergedNonFinalStates(nonFinalStates, finalStates));

        Set<Transition> mergedTransitions = getMergedTransitions(minimizedAutomaton, mergedStates);

        return new Automaton(automaton.getTerminals(), mergedStates.toArray(new State[0]),
                mergedTransitions.toArray(new Transition[0]), initialState,
                mergedFinalStates.toArray(new State[0]));
    }

    private static Automaton cloneAutomaton(Automaton automaton) {
        return new Automaton(automaton.getTerminals().clone(), cloneStates(automaton.getStates()),
                cloneTransitions(automaton.getTransitions()), automaton.getInState(),
                automaton.getFiStates().clone());
    }

    private static State[] cloneStates(State[] states) {
        State[] clonedStates = new State[states.length];
        for (int i = 0; i < states.length; i++) {
            clonedStates[i] = new State(states[i].getStateNumber(), states[i].isFinal());
        }
        return clonedStates;
    }

    private static Transition[] cloneTransitions(Transition[] transitions) {
        Transition[] clonedTransitions = new Transition[transitions.length];
        for (int i = 0; i < transitions.length; i++) {
            clonedTransitions[i] = new Transition(transitions[i].getSourceState(),
                    transitions[i].getTerminal(), transitions[i].getTargetState());
        }
        return clonedTransitions;
    }

    private static Set<Set<State>> splitStateSet(Set<State> stateSet, Automaton automaton) {
        Set<Set<State>> partitions = new HashSet<>();
        Set<State> visited = new HashSet<>();
        for (State state : stateSet) {
            if (!visited.contains(state)) {
                Set<State> equivalentStates = getEquivalentStates(state, stateSet, automaton);
                partitions.add(equivalentStates);
                visited.addAll(equivalentStates);
            }
        }
        return partitions;
    }

    private static Set<State> getEquivalentStates(State state, Set<State> stateSet, Automaton automaton) {
        Set<State> equivalentStates = new HashSet<>();
        equivalentStates.add(state);
        for (State otherState : stateSet) {
            if (otherState.equals(state)) {
                continue;
            }
            if (areEquivalentStates(state, otherState, automaton)) {
                equivalentStates.add(otherState);
            }
        }
        return equivalentStates;
    }

    private static boolean areEquivalentStates(State state1, State state2, Automaton automaton) {
        for (char terminal : automaton.getTerminals()) {
            State nextState1 = automaton.getNextState(state1, terminal);
            State nextState2 = automaton.getNextState(state2, terminal);
            if (!nextState1.equals(nextState2)) {
                return false;
            }
        }
        return true;
    }

    private static boolean containsFinalState(Set<State> stateSet) {
        for (State state : stateSet) {
            if (state.isFinal()) {
                return true;
            }
        }
        return false;
    }

    private static State getMergedState(State state, Set<State> finalStates) {
        if (finalStates.contains(state)) {
            return new State(0, true);
        } else {
            return new State(0, false);
        }
    }

    private static List<State> getMergedNonFinalStates(Set<State> nonFinalStates, Set<State> finalStates) {
        List<State> mergedNonFinalStates = new ArrayList<>();
        for (State state : nonFinalStates) {
            if (!finalStates.contains(state)) {
                mergedNonFinalStates.add(new State(0, false));
            }
        }
        return mergedNonFinalStates;
    }

    private static Set<Transition> getMergedTransitions(Automaton automaton, List<State> mergedStates) {
        Set<Transition> mergedTransitions = new HashSet<>();
        for (State state : mergedStates) {
            for (char terminal : automaton.getTerminals()) {
                State nextState = automaton.getNextState(state, terminal);
                if (nextState != null) {
                    State mergedNextState = getMergedState(nextState, new HashSet<>(Arrays.asList(automaton.getFiStates())));

                    mergedTransitions.add(new Transition(state, terminal, mergedNextState));
                }
            }
        }
        return mergedTransitions;
    }
}
