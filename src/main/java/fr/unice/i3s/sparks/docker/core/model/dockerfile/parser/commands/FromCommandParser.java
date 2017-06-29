package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;

import java.util.Iterator;

public class FromCommandParser implements CommandParser {
    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {
        String[] split = currentLine.split(" ");
        String body = split[split.length - 1];
        return new FROMCommand(new ImageID(body));
    }
}
