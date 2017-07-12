package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

public class NonParsedCommand extends Command {
    private String s;

    public NonParsedCommand(String s) {

        if(s.toLowerCase().startsWith("run")) {
            //System.err.println("PARSEDCOMMAND THAT CONTAINS RUN:" + s);
        }

        this.s = s;
    }
}
