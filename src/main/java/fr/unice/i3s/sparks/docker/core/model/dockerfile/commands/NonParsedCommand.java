package fr.unice.i3s.sparks.docker.core.model.dockerfile.commands;

public class NonParsedCommand extends Command {
    private String s;

    public NonParsedCommand(String s) throws Exception {

        if(s.toLowerCase().startsWith("run")) {
            throw new Exception("PARSEDCOMMAND THAT CONTAINS RUN:" + s);
        }

        this.s = s;
    }
}
