package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ENTRYPointCommand extends Command {
    private List body;

    public ENTRYPointCommand(String... body) {
        this.body = new ArrayList<>(Arrays.asList(body));
    }
}
