package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.Arrays;
import java.util.List;

public class AptUpdate extends ShellCommand {

    public AptUpdate(List<String> body) {
        super(body);
    }

    public AptUpdate(String... body) {
        super(Arrays.asList(body));
    }
}
