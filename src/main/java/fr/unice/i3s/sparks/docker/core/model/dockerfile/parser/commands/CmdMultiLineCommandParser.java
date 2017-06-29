package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;

import java.util.Iterator;
import java.util.regex.Pattern;

//  Fixme/todo must be handled as RUNCommand (replace ShellCommand?)
public class CmdMultiLineCommandParser implements CommandParser {
    public static final Pattern CMD_AGAIN_MULTILINE = Pattern.compile(".*(\\\\)+\\s*");
    public static final Pattern CMD_END_MULTILINE = Pattern.compile("[^\\\\]*");

    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {
        String line = currentLine;
        String body = "";
        while (CMD_AGAIN_MULTILINE.matcher(line).matches() || line.trim().isEmpty() || line.trim().startsWith("#")) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                line = iterator.next();
                continue;
            }
            body += " " + line;
            line = iterator.next();
        }

        if (CMD_END_MULTILINE.matcher(line).matches()) {
            body += " " + line;
        }
        CMDCommand cmdCommand = new CMDCommand(body);
        return cmdCommand;
    }
}
