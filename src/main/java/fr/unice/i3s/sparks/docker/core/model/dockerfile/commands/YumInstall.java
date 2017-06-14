package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.Arrays;
import java.util.List;

public class YumInstall extends ShellCommand {

    public YumInstall(List<String> body) {
        super(body);
    }

    public YumInstall(String... body) {
        super(Arrays.asList(body));
    }
}
