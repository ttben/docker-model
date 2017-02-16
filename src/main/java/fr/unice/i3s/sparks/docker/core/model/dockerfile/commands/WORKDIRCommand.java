package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

public class WORKDIRCommand extends Command {
    private String body;

    public WORKDIRCommand(String body) {
        this.body = body;
    }
}
