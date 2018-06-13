package fr.unice.i3s.sparks.docker.core.model.dockerfile;

import fr.uca.i3s.sparks.composition.metamodel.Action;
import fr.uca.i3s.sparks.composition.metamodel.Artefact;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dockerfile extends Artefact<Command> {
    private String sourceFile;

    private Dockerfile parent;
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

    public Dockerfile getParent() {
        return parent;
    }

    public void setParent(Dockerfile parent) {
        this.parent = parent;
    }

    //  todo push to the metamodel
    public boolean containsTag(Class<? extends Tag> tag) {
        for (Command a : actions) {
            if (a.containsTag(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int howMuch(Class<? extends Action> commandClass) {
        if (ShellCommand.class.isAssignableFrom(commandClass)) {
            return deepCount(commandClass);
        } else {
            return super.howMuch(commandClass);
        }
    }

    private int deepCount(Class<? extends Action> commandClass) {
        int result = 0;

        if (commandClass.equals(ShellCommand.class)) {
            for (RUNCommand runCommand : getActionsOfType(RUNCommand.class)) {
                List<ShellCommand> body = runCommand.getBody();
                result += body.size();
            }
            return result;
        }

        List<RUNCommand> actionsOfType = getActionsOfType(RUNCommand.class);
        for (RUNCommand runCommand : actionsOfType) {
            List<ShellCommand> body = runCommand.getBody();
            for (ShellCommand shellCommand : body) {
                if (shellCommand.getClass().isAssignableFrom(commandClass) && !shellCommand.getClass().equals(ShellCommand.class)) {
                    result++;
                }
            }
        }
        return result;
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
