package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CMDTest {

    @Test
    public void multilinesCMDCommand() throws IOException {
        File f = new File(RUNTest.class.getClassLoader().getResource("MultiLinesCMD").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(1, result.getActions().size());
        assertEquals(1, result.howMuch(CMDCommand.class));

    }
}
