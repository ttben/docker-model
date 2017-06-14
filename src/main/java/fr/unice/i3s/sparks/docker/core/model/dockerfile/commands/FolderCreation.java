package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.Arrays;
import java.util.List;

public class FolderCreation extends ShellCommand {

    public FolderCreation(List<String> body) {
        super(body);
    }

    public FolderCreation(String... body) {
        super(Arrays.asList(body));
    }
}
