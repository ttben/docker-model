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

public class RUNTest {


    @Test
    public void simpleRunOneLine() {
        Dockerfile result = new Dockerfile();
        List<String> strings = Arrays.asList("RUN apt-get update && apt-get install -y git wget rsync php5 php5-curl curl zip");

        DockerFileParser.parseLines(strings, result);

        assertFalse(result.getActions().isEmpty());
        assertEquals(RUNCommand.class, result.getActions().get(0).getClass());
        assertEquals(1, result.getActions().size());
    }

    @Test
    public void complexRunOneLine() {
        Dockerfile result = new Dockerfile();
        List<String> strings = Arrays.asList("RUN sed 's@session\\s*required\\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd");

        DockerFileParser.parseLines(strings, result);

        assertEquals(1, result.getActions().size());
        assertEquals(RUNCommand.class, result.getActions().get(0).getClass());
    }

    @Test
    public void simpleRunMultiLines() {
        Dockerfile result = new Dockerfile();
        List<String> strings = Arrays.asList("RUN apt-get update \\\n&& apt-get install -y git wget rsync php5 php5-curl curl zip");

        DockerFileParser.parseLines(strings, result);

        assertFalse(result.getActions().isEmpty());
        assertEquals(RUNCommand.class, result.getActions().get(0).getClass());
        assertEquals(1, result.getActions().size());
    }

    @Test
    public void complexRunMultiLines() {
        Dockerfile result = new Dockerfile();
        List<String> strings = Arrays.asList("RUN sed 's@session\\s*required\\s*pam_loginuid.so@session optional pam_loginuid.so@g'\\\n -i /etc/pam.d/sshd");

        DockerFileParser.parseLines(strings, result);

        assertEquals(1, result.getActions().size());
        assertEquals(RUNCommand.class, result.getActions().get(0).getClass());
    }

    @Test
    public void complexRunMultiLines2() throws IOException {
        File f = new File(RUNTest.class.getClassLoader().getResource("Dockerfiletest").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(3, result.getActions().size());
        assertEquals(FROMCommand.class, result.getActions().get(0).getClass());
        assertEquals(RUNCommand.class, result.getActions().get(1).getClass());
        assertEquals(WORKDIRCommand.class, result.getActions().get(2).getClass());
    }

    @Test
    public void complexRunMultiLines3() throws IOException {
        File f = new File(RUNTest.class.getClassLoader().getResource("ComplexMutliRunWithWS").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(4, result.getActions().size());
        assertEquals(FROMCommand.class, result.getActions().get(0).getClass());
        assertEquals(RUNCommand.class, result.getActions().get(1).getClass());
        assertEquals(RUNCommand.class, result.getActions().get(2).getClass());
        assertEquals(RUNCommand.class, result.getActions().get(3).getClass());
    }

    @Test
    public void complexRunMultiLines4() throws IOException {
        File f = new File(RUNTest.class.getClassLoader().getResource("ComplexMutliRunWithEmptyLines").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(13, result.getActions().size());
    }

    @Test
    public void realLife1() throws IOException {
        File f = new File(RUNTest.class.getClassLoader().getResource("RealLifeRun1").getPath());
        Dockerfile result = DockerFileParser.parse(f);
        assertEquals(8, result.getActions().size());

        assertEquals(1, result.howMuch(FROMCommand.class));
        assertEquals(1, result.howMuch(MAINTAINERCommand.class));
        assertEquals(3, result.howMuch(RUNCommand.class));  // last one is faulty
        assertEquals(1, result.howMuch(ENTRYPointCommand.class));
        assertEquals(1, result.howMuch(ENVCommand.class));

        assertEquals(0, result.howMuch(ARGCommand.class));
        assertEquals(0, result.howMuch(COPYCommand.class));
        assertEquals(0, result.howMuch(WORKDIRCommand.class));
        assertEquals(0, result.howMuch(CMDCommand.class));
        assertEquals(0, result.howMuch(ONBUILDCommand.class));
    }
}