package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

public class USERCommand extends Command {
    private String body;

    public USERCommand(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
