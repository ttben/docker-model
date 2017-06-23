package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LABELCommand extends Command {
    private List<String> body = new ArrayList<>();

    public LABELCommand(String... body) {
        this.body = new ArrayList<>(Arrays.asList(body));
    }

    public List<String> getBody() {
        return body;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LABELCommand{");
        sb.append("body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
