package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.LABELCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;

import java.util.Iterator;
import java.util.regex.Pattern;

public class LabelMultiLineCommandParser implements CommandParser {
    public static final Pattern LABEL_AGAIN_MULTILINE = Pattern.compile(".*(\\\\)+\\s*");
    public static final Pattern LABEL_END_MULTILINE = Pattern.compile("[^\\\\]*");

    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {
        String body = "";
        String line = currentLine;

        while (LABEL_AGAIN_MULTILINE.matcher(line).matches() || line.trim().isEmpty() || line.trim().startsWith("#")) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                line = iterator.next();
                continue;
            }
            body += " " + line;
            line = iterator.next();
        }

        if (LABEL_END_MULTILINE.matcher(line).matches()) {
            body += " " + line;
        }

        return new LABELCommand(body);
    }
}
