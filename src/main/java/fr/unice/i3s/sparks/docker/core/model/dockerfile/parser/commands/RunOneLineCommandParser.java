package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RunOneLineCommandParser implements CommandParser {
    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {
        List<ShellCommand> shellCommands = buildRunCommand(currentLine, true);
        RUNCommand runCommand = new RUNCommand(shellCommands);
        return runCommand;
    }

    private static List<ShellCommand> buildRunCommand(String line, boolean isFirstLineOfRun) {

        String[] split = null;

        line = line.trim();
        if (isFirstLineOfRun) {
            String s = line.substring(4);
            if (s.trim().startsWith("[")) {
                s = s.substring(1); // omit [
                s = s.substring(0, s.length() - 1); //   omit ]
                split = s.split(",");

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

            } else {
                split = line.split("&&");

                split[0] = split[0].substring(4);
            }
        }
        List<ShellCommand> shellCommands = new ArrayList<>();
        for (String s : split) {
            s = s.trim();
            ShellCommand shellCommand = new ShellCommand(s.split(" "));
            shellCommands.add(shellCommand);
        }

        return shellCommands;
    }
}
