package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMDCommand extends Command{

    private Dockerfile dockerfile;
    private final List<String> body;

    public CMDCommand(Dockerfile dockerfile, String... body) {
        this.dockerfile = dockerfile;
        this.body = new ArrayList<>(Arrays.asList(body));
    }

    public CMDCommand(String... body) {
        this.dockerfile = null;
        this.body = new ArrayList<>(Arrays.asList(body));
    }

    public Dockerfile getParent() {
        return this.dockerfile;
    }

    public List<String> getBody() {
        return body;
    }
}
