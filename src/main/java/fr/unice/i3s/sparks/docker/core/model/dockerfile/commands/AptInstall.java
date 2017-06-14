package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.Arrays;
import java.util.List;

public class AptInstall extends ShellCommand {

    public AptInstall(List<String> body) {
        super(body);
    }

    public AptInstall(String... body) {
        super(Arrays.asList(body));
    }
}
