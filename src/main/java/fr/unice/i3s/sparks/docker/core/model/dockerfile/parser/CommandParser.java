package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;

import java.util.Iterator;

public interface CommandParser {
    Command parse(Iterator<String> iterator, String currentLine);
}
