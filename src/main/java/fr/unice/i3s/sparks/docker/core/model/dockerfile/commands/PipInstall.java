package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.Arrays;
import java.util.List;

public class PipInstall extends ShellCommand {

    public PipInstall(List<String> body) {
        super(body);
    }

    public PipInstall(String... body) {
        super(Arrays.asList(body));
    }
}
