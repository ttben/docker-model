package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.EnvKeyValue;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DockerFileParser {

    public static Dockerfile parse(File file) throws IOException {
        System.err.println("Handling file:" + file.getAbsolutePath());
        Path path = Paths.get(file.getAbsolutePath());
        Stream<String> fileLines = Files.lines(path);
        Collector<String, ?, ArrayList<String>> stringArrayListCollector = Collectors.toCollection(ArrayList::new);
        ArrayList<String> lines = fileLines.collect(stringArrayListCollector);

        Dockerfile result = new Dockerfile(file.getAbsolutePath());

        parseLines(lines, result);

        fileLines.close();

        return result;
    }

    public static void parseLines(List<String> lines, Dockerfile result) {
        ListIterator<String> stringListIterator = lines.listIterator();


        while (stringListIterator.hasNext()) {
            String line = stringListIterator.next();

            line = line.replace('\t', ' ');
            line = line.replaceAll("(\\s)+", " ");
            line = line.trim();

            if (line.trim().startsWith("#") | line.trim().isEmpty()) {
                continue;
            }

            CommandParser dispatch = CommandParserDispatcher.dispatch(line);
            Command parse = dispatch.parse(stringListIterator, line);
            result.add(parse);
        }
    }
}