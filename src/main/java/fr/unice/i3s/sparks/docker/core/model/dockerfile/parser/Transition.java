package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

public  abstract class Transition<R> {
    private State next;

    public abstract boolean accept(String s, Integer index);

    public State getNext() {
        return next;
    }

    public void setNext(State next) {
        this.next = next;
    }
}
