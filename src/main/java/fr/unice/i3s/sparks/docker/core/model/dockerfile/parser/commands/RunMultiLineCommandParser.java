package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class RunMultiLineCommandParser implements CommandParser {
    public static final Pattern RUN_AGAIN_MULTILINE = Pattern.compile(".*(\\\\)+\\s*");
    public static final Pattern RUN_END_MULTILINE = Pattern.compile("[^\\\\]*");

    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {

        currentLine = currentLine.trim();

        while(currentLine.startsWith("\t")) {
            currentLine = currentLine.substring(1, currentLine.length());
        }

        if (currentLine.startsWith("\\")) {
            currentLine = currentLine.substring(1, currentLine.length());
        }

        if (currentLine.endsWith("\\")) {
            currentLine = currentLine.substring(0, currentLine.length()-1);
        }
        String completeFullLine  = currentLine;

        List<ShellCommand> shellCommands = buildRunCommand(currentLine, true);
        String line = iterator.next();

        while (RUN_AGAIN_MULTILINE.matcher(line).matches() || line.trim().isEmpty() || line.trim().startsWith("#")) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                line = iterator.next();
                continue;
            }
            line = line.trim();

            while(line.startsWith("\t")) {
                line = line.substring(1, line.length());
            }

            if (line.startsWith("\\")) {
                line = line.substring(1, line.length());
            }

            if (line.endsWith("\\")) {
                line = line.substring(0, line.length()-1);
            }
            completeFullLine += " "+ line;

            shellCommands.addAll(buildRunCommand(line, false));
            line = iterator.next();
        }

        if (RUN_END_MULTILINE.matcher(line).matches()) {
            line = line.trim();

            while(line.startsWith("\t")) {
                line = line.substring(1, line.length());
            }
            if (line.startsWith("\\")) {
                line = line.substring(1, line.length());
            }

            if (line.endsWith("\\")) {
                line = line.substring(0, line.length()-1);
            }
            completeFullLine += " "+ line;

            shellCommands.addAll(buildRunCommand(line, false));
        }

        Command parse = new RunOneLineCommandParser().parse(null, completeFullLine);

        return parse;
    }

    private static List<ShellCommand> buildRunCommand(String line, boolean isFirstLineOfRun) {

        String[] split = null;

        if (isFirstLineOfRun) {
            line = line.substring(4);
        }

        if (line.trim().startsWith("[")) {
            line = line.substring(1); // omit [
            line = line.substring(0, line.length() - 1); //   omit ]
            split = line.split(",");

            for (int i = 0; i < split.length; i++) {
                String str = split[i];
                str = str.trim();
                str = str.substring(1); // omit "
                str = str.substring(0, str.length() - 1); // omit "
                str = str.trim();
                split[i] = str;
            }

            String join = String.join(" ", split);
            split = new String[1];
            split[0] = join;

        } else if (line.isEmpty()) {
            return new ArrayList<>();
        } else {
            split = line.split("&&");
        }

        List<ShellCommand> shellCommands = new ArrayList<>();
        for (String s : split) {
            s = s.trim();
            if (s.isEmpty()) {
                continue;
            }
            ShellCommand shellCommand = new ShellCommand(s.split(" "));
            shellCommands.add(shellCommand);
        }

        return shellCommands;
    }
}
