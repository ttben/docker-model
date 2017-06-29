package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;

import java.util.ArrayList;
import java.util.List;

public class State {
    private boolean isInitial;
    private boolean isFinal;
    private List<Transition> outTransitions = new ArrayList<>();

    public State(boolean isInitial, boolean isFinal) {
        this.isInitial = isInitial;
        this.isFinal = isFinal;
    }

    public State(boolean isInitial, boolean isFinal, List<Transition> outTransitions) {
        this.isInitial = isInitial;
        this.isFinal = isFinal;
        this.outTransitions = outTransitions;
    }

    public State() {

    }

    public boolean isInitial() {
        return isInitial;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public List<Transition> getOutTransitions() {
        return outTransitions;
    }

    public void attachTransition(Transition transition) {
        this.outTransitions.add(transition);
    }

    public List<ENVCommand> accept(String input, Integer index) {
        for (Transition transition : outTransitions) {
            if (transition.accept(input, index)) {
                State next = transition.getNext();
                List<ENVCommand> accept = next.accept(input, index);
                return accept;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("State{");
        sb.append("isInitial=").append(isInitial);
        sb.append(", isFinal=").append(isFinal);
        sb.append(", outTransitions=").append(outTransitions);
        sb.append('}');
        return sb.toString();
    }
}
