package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.EnvKeyValue;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.CommandParser;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.ENVAutomata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class EnvMultiLineCommandParser implements CommandParser {
    public static final Pattern ENV_AGAIN_MULTILINE = Pattern.compile(".*(\\\\)+\\s*");
    public static final Pattern ENV_END_MULTILINE = Pattern.compile("[^\\\\]*");

    @Override
    public Command parse(Iterator<String> iterator, String currentLine) {
        String line = currentLine.trim();
        List<EnvKeyValue> keyValuePairs = buildMultiLinesEnvCommand(line, true);
        line = iterator.next();

        while (ENV_AGAIN_MULTILINE.matcher(line).matches()
                || line.trim().isEmpty() || line.trim().startsWith("#")) {
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                line = iterator.next();
                continue;
            }
            keyValuePairs.addAll(buildMultiLinesEnvCommand(line, false));
            line = iterator.next();
        }

        if (ENV_END_MULTILINE.matcher(line).matches()) {
            keyValuePairs.addAll(buildMultiLinesEnvCommand(line, false));
        }
        return new ENVCommand(keyValuePairs);
    }

    private static List<EnvKeyValue> buildMultiLinesEnvCommand(String line, boolean isFirstLineOfEnv) {
        line = line.trim();

        if (isFirstLineOfEnv) {
            //  Delete env command
            line = line.substring(3, line.length());
            line = line.trim();
        }

        if (line.endsWith("\\")) {
            //  Delete '\' suffix
            line = line.substring(0, line.length() - 1);
            line = line.trim();
        }


        List<EnvKeyValue> envKeyValues = new ArrayList<>();

        //  Can happened with 'ENV \'
        if (line.isEmpty()) {
            return envKeyValues;
        }

        char[] charArray = line.toCharArray();

        ENVAutomata envAutomata = new ENVAutomata();
        envAutomata.handle(charArray, 0);
        envKeyValues = envAutomata.getEnvKeyValues();

        return envKeyValues;
    }
}