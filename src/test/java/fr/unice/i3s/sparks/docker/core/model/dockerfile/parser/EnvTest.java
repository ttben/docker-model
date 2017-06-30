package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class EnvTest {

    @Test
    public void multilinesEnvCommand() throws IOException {
        File f = new File(RUNTest.class.getClassLoader().getResource("MultilinesENVCommand").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(10, result.getActions().size());
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
        File f = new File(RUNTest.class.getClassLoader().getResource("EnvDKFRef").getPath());
        Dockerfile result = DockerFileParser.parse(f);

        assertEquals(4, result.getActions().size());
        assertEquals(4, result.howMuch(ENVCommand.class));

        assertEquals("myName", ((ENVCommand) result.getActions().get(0)).getEnvKeyValues().get(0).getKey());
        assertEquals("myDog", ((ENVCommand) result.getActions().get(0)).getEnvKeyValues().get(1).getKey());
        assertEquals("myCat", ((ENVCommand) result.getActions().get(0)).getEnvKeyValues().get(2).getKey());
        assertEquals("myName", ((ENVCommand) result.getActions().get(1)).getEnvKeyValues().get(0).getKey());
        assertEquals("myDog", ((ENVCommand) result.getActions().get(2)).getEnvKeyValues().get(0).getKey());
        assertEquals("myCat", ((ENVCommand) result.getActions().get(3)).getEnvKeyValues().get(0).getKey());

        assertEquals("John Doe", ((ENVCommand) result.getActions().get(0)).getEnvKeyValues().get(0).getValue());
        assertEquals("Rex\\ The\\ Dog", ((ENVCommand) result.getActions().get(0)).getEnvKeyValues().get(1).getValue());
        assertEquals("fluffy", ((ENVCommand) result.getActions().get(0)).getEnvKeyValues().get(2).getValue());
        assertEquals("John Doe", ((ENVCommand) result.getActions().get(1)).getEnvKeyValues().get(0).getValue());
        assertEquals("Rex The Dog", ((ENVCommand) result.getActions().get(2)).getEnvKeyValues().get(0).getValue());
        assertEquals("fluffy", ((ENVCommand) result.getActions().get(3)).getEnvKeyValues().get(0).getValue());
    }

    @Test
    public void realLifeEnv1() throws IOException {
        File f = new File(RUNTest.class.getClassLoader().getResource("RealLifeEnv1").getPath());
        Dockerfile result = DockerFileParser.parse(f);
        assertEquals(9, result.getActions().size());
        assertEquals(1, result.howMuch(FROMCommand.class));
        assertEquals(1, result.howMuch(MAINTAINERCommand.class));
        assertEquals(1, result.howMuch(ARGCommand.class));
        assertEquals(1, result.howMuch(COPYCommand.class));
        assertEquals(1, result.howMuch(ENVCommand.class));
        assertEquals(1, result.howMuch(RUNCommand.class));
        assertEquals(1, result.howMuch(WORKDIRCommand.class));
        assertEquals(1, result.howMuch(CMDCommand.class));
        assertEquals(1, result.howMuch(ONBUILDCommand.class));
    }

    @Test
    public void realLifeEnv2() throws IOException {
        File f = new File(RUNTest.class.getClassLoader().getResource("RealLifeEnv2").getPath());
        Dockerfile result = DockerFileParser.parse(f);
        assertEquals(25, result.getActions().size());
        assertEquals(1, result.howMuch(FROMCommand.class));
        assertEquals(2, result.howMuch(LABELCommand.class));
        assertEquals(3, result.howMuch(ENVCommand.class));
        assertEquals(5, result.howMuch(RUNCommand.class));
        assertEquals(1, result.howMuch(COPYCommand.class));
        assertEquals(1, result.howMuch(WORKDIRCommand.class));
        assertEquals(1, result.howMuch(CMDCommand.class));
        assertEquals(1, result.howMuch(VOLUMECommand.class));
        assertEquals(1, result.howMuch(EXPOSECommand.class));

        assertEquals(0, result.howMuch(MAINTAINERCommand.class));
        assertEquals(0, result.howMuch(ARGCommand.class));
        assertEquals(0, result.howMuch(ONBUILDCommand.class));
    }
}
