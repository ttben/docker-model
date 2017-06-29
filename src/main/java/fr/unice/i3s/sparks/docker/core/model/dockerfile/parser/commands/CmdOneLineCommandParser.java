package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;

import java.util.Arrays;
import java.util.Iterator;

public class CmdOneLineCommandParser implements CommandParser {
    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {
        String[] split = currentLine.split(" ");

        String[] body = Arrays.copyOfRange(split, 1, split.length);

        CMDCommand cmdCommand = new CMDCommand(body);
        return cmdCommand;
    }
}
