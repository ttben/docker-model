package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RUNCommand extends Command {

    private List<ShellCommand> body = new ArrayList<>();

    public RUNCommand() {
    }

    public RUNCommand(List<ShellCommand> body) {
        this.body = body;
    }

    public RUNCommand(ShellCommand... body) {
        this.body = Arrays.asList(body);
    }

    public void add(ShellCommand shellCommand) {
        this.body.add(shellCommand);
    }

    public List<ShellCommand> getBody() {
        return body;
    }

    public void setBody(List<ShellCommand> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RUNCommand{");
        sb.append("body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
