package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

public class VOLUMECommand extends Command {
    private String body;

    public VOLUMECommand(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
