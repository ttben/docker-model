package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.EXPOSECommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;

import java.util.Arrays;
import java.util.Iterator;

public class ExposeCommandParser implements CommandParser {
    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {
        String[] split = currentLine.split(" ");
        String[] body = Arrays.copyOfRange(split, 1, split.length);
        return new EXPOSECommand(body);
    }
}
