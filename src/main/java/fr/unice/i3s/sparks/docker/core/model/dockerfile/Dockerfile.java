package fr.unice.i3s.sparks.docker.core.model.dockerfile;

import fr.uca.i3s.sparks.composition.metamodel.Artefact;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dockerfile extends Artefact<Command> {
    private String sourceFile;

    public Dockerfile(){
        actions = new ArrayList<>();
    }

    public Dockerfile(List<Command> listOfCommand) {

        this.actions = listOfCommand;
    }

    public Dockerfile(Command... commands) {
        this.actions = new ArrayList<>(Arrays.asList(commands));
    }

    public Dockerfile(String absolutePath) {
        this();
        sourceFile = absolutePath;
    }

    public Dockerfile(List<Command> listOfCommand, String absolutePath) {
        this(listOfCommand);
        sourceFile = absolutePath;
    }

    public Dockerfile(String absolutePath, Command... commands) {
        this(commands);
        sourceFile = absolutePath;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Dockerfile{");
        sb.append("actions=").append(actions);
        sb.append('}');
        return sb.toString();
    }
}
