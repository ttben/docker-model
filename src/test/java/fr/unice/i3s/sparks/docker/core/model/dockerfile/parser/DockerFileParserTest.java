package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DockerFileParserTest {


    @Test
    public void simpleRunOneLine() {
        Dockerfile result = new Dockerfile();
        List<String> strings = Arrays.asList("RUN apt-get update && apt-get install -y git wget rsync php5 php5-curl curl zip");

        DockerFileParser.parseLines(strings, result);

        assertFalse(result.getListOfCommand().isEmpty());
        assertEquals(RUNCommand.class, result.getListOfCommand().get(0).getClass());
        assertEquals(1, result.getListOfCommand().size());
    }

    @Test
    public void complexRunOneLine() {
        Dockerfile result = new Dockerfile();
        List<String> strings = Arrays.asList("RUN sed 's@session\\s*required\\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd");

        DockerFileParser.parseLines(strings, result);

        assertEquals(1, result.getListOfCommand().size());
        assertEquals(RUNCommand.class, result.getListOfCommand().get(0).getClass());
    }

    @Test
    public void simpleRunMultiLines() {
        Dockerfile result = new Dockerfile();
        List<String> strings = Arrays.asList("RUN apt-get update \\\n&& apt-get install -y git wget rsync php5 php5-curl curl zip");

        DockerFileParser.parseLines(strings, result);

        assertFalse(result.getListOfCommand().isEmpty());
        assertEquals(RUNCommand.class, result.getListOfCommand().get(0).getClass());
        assertEquals(1, result.getListOfCommand().size());
    }

    @Test
    public void complexRunMultiLines() {
        Dockerfile result = new Dockerfile();
        List<String> strings = Arrays.asList("RUN sed 's@session\\s*required\\s*pam_loginuid.so@session optional pam_loginuid.so@g'\\\n -i /etc/pam.d/sshd");

        DockerFileParser.parseLines(strings, result);

        assertEquals(1, result.getListOfCommand().size());
        assertEquals(RUNCommand.class, result.getListOfCommand().get(0).getClass());
    }

    @Test
    public void complexRunMultiLines2() throws IOException {
        File f = new File(DockerFileParserTest.class.getClassLoader().getResource("Dockerfiletest").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(3, result.getListOfCommand().size());
        assertEquals(FROMCommand.class, result.getListOfCommand().get(0).getClass());
        assertEquals(RUNCommand.class, result.getListOfCommand().get(1).getClass());
        assertEquals(WORKDIRCommand.class, result.getListOfCommand().get(2).getClass());
    }

    @Test
    public void complexRunMultiLines3() throws IOException {
        File f = new File(DockerFileParserTest.class.getClassLoader().getResource("ComplexMutliRunWithWS").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(4, result.getListOfCommand().size());
        assertEquals(FROMCommand.class, result.getListOfCommand().get(0).getClass());
        assertEquals(RUNCommand.class, result.getListOfCommand().get(1).getClass());
        assertEquals(RUNCommand.class, result.getListOfCommand().get(2).getClass());
        assertEquals(RUNCommand.class, result.getListOfCommand().get(3).getClass());
    }

    @Test
    public void complexRunMultiLines4() throws IOException {
        File f = new File(DockerFileParserTest.class.getClassLoader().getResource("ComplexMutliRunWithEmptyLines").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(13, result.getListOfCommand().size());
    }

    @Test
    public void multilinesEnvCommand() throws IOException {
        File f = new File(DockerFileParserTest.class.getClassLoader().getResource("MultilinesENVCommand").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(10, result.getListOfCommand().size());
        assertEquals(1, result.howMuch(FROMCommand.class));
        assertEquals(1, result.howMuch(ENVCommand.class));
        assertEquals(3, result.howMuch(RUNCommand.class));
        assertEquals(1, result.howMuch(VOLUMECommand.class));
        assertEquals(1, result.howMuch(COPYCommand.class));
        assertEquals(1, result.howMuch(WORKDIRCommand.class));
        assertEquals(1, result.howMuch(USERCommand.class));
        assertEquals(1, result.howMuch(CMDCommand.class));
    }

    @Test
    public void multilinesEnvCommandDKFRef() throws IOException {
        File f = new File(DockerFileParserTest.class.getClassLoader().getResource("EnvDKFRef").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(4, result.getListOfCommand().size());
        assertEquals(4, result.howMuch(ENVCommand.class));

        assertEquals("myName", ((ENVCommand) result.getListOfCommand().get(0)).getEnvKeyValues().get(0).getKey());
        assertEquals("myDog", ((ENVCommand) result.getListOfCommand().get(0)).getEnvKeyValues().get(1).getKey());
        assertEquals("myCat", ((ENVCommand) result.getListOfCommand().get(0)).getEnvKeyValues().get(2).getKey());
        assertEquals("myName", ((ENVCommand) result.getListOfCommand().get(1)).getEnvKeyValues().get(0).getKey());
        assertEquals("myDog", ((ENVCommand) result.getListOfCommand().get(2)).getEnvKeyValues().get(0).getKey());
        assertEquals("myCat", ((ENVCommand) result.getListOfCommand().get(3)).getEnvKeyValues().get(0).getKey());

        assertEquals("John Doe", ((ENVCommand) result.getListOfCommand().get(0)).getEnvKeyValues().get(0).getValue());
        assertEquals("Rex\\ The\\ Dog", ((ENVCommand) result.getListOfCommand().get(0)).getEnvKeyValues().get(1).getValue());
        assertEquals("fluffy", ((ENVCommand) result.getListOfCommand().get(0)).getEnvKeyValues().get(2).getValue());
        assertEquals("John Doe", ((ENVCommand) result.getListOfCommand().get(1)).getEnvKeyValues().get(0).getValue());
        assertEquals("Rex The Dog", ((ENVCommand) result.getListOfCommand().get(2)).getEnvKeyValues().get(0).getValue());
        assertEquals("fluffy", ((ENVCommand) result.getListOfCommand().get(3)).getEnvKeyValues().get(0).getValue());
    }

    @Test
    public void multilinesCMDCommand() throws IOException {
        File f = new File(DockerFileParserTest.class.getClassLoader().getResource("MultiLinesCMD").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(1, result.getListOfCommand().size());
        assertEquals(1, result.howMuch(CMDCommand.class));

    }

}