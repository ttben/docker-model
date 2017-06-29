package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.EnvKeyValue;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;

import java.util.Arrays;
import java.util.Iterator;

// fixme
public class EnvOneLineCommandParser implements CommandParser {
    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {
        String line = currentLine.trim();


        if (line.toLowerCase().startsWith("env")) {
            //  Delete env command
            line = line.substring(3, line.length());
            line = line.trim();
        }

        if (line.endsWith("\\")) {
            //  Delete '\' suffix
            line = line.substring(0, line.length() - 1);
            line = line.trim();
        }


        String[] body = line.split(" ");

        String key, value = "";

        key = body[0];
        for (int i = 1; i < body.length; i++) {
            if (i != 1) {
                value += " ";
            }
            value += body[i];
        }


        ENVCommand envCommand = new ENVCommand(Arrays.asList(new EnvKeyValue(key, value)));
        return envCommand;
    }
}
