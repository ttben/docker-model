package fr.unice.i3s.sparks.docker.core.model.dockerfile;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dockerfile {
    private List<Command> listOfCommand;
    private String sourcefIle;

    public Dockerfile(){
        listOfCommand = new ArrayList<>();
    }

    public Dockerfile(List<Command> listOfCommand) {

        this.listOfCommand = listOfCommand;
    }

    public Dockerfile(Command... commands) {
        this.listOfCommand = new ArrayList<>(Arrays.asList(commands));
    }

    public Dockerfile(String absolutePath) {
        this();
        sourcefIle = absolutePath;
    }

    public Dockerfile(List<Command> listOfCommand, String absolutePath) {
        this(listOfCommand);
        sourcefIle = absolutePath;
    }

    public Dockerfile(String absolutePath, Command... commands) {
        this(commands);
        sourcefIle = absolutePath;
    }

    public String getSourcefIle() {
        return sourcefIle;
    }

    public List<Command> getListOfCommand() {
        return listOfCommand;
    }

    public <T extends Command> List<T> getCommandsOfType(Class<T> clazz) {
        List<T> lol = new ArrayList<>();

        for (Command c : listOfCommand) {
            if (c.getClass().equals(clazz)) {
                lol.add((T) c);
            }
        }

        return lol;
    }

    public void addCommand(Command command) {
        this.listOfCommand.add(command);
    }

    public void addCommands(List<Command> commands) {
        this.listOfCommand.addAll(commands);
    }

    public int howMuch(Class<? extends Command> commandClass) {
        int result = 0;
        for (Command command : listOfCommand) {
            if (command.getClass().equals(commandClass)) {
                result++;
            } else if(command instanceof RUNCommand) {
                List<ShellCommand> body = ((RUNCommand) command).getBody();
                for (ShellCommand shellCommand:body) {
                    if (shellCommand.getClass().equals(commandClass)) {
                        result++;
                    }
                }
            }
        }

        return result;
    }

    public boolean contains(Class<? extends Command> commandClass) {
        for (Command command : listOfCommand) {
            if (command.getClass().equals(commandClass)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Dockerfile{");
        sb.append("listOfCommand=").append(listOfCommand);
        sb.append('}');
        return sb.toString();
    }

    public boolean deepContains(Class<? extends Command> commandClass) {
        for (Command command : listOfCommand) {
            if (command.getClass().equals(commandClass)) {
                return true;
            }

            if(command instanceof RUNCommand) {
                List<ShellCommand> body = ((RUNCommand) command).getBody();
                for(ShellCommand shellCommand : body) {
                    if(shellCommand.getClass().equals(commandClass)) {
                        return true;
                    }
                }
            }

        }

        return false;
    }
}
