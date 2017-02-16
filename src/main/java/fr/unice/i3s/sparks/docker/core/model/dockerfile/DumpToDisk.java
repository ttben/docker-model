package fr.unice.i3s.sparks.docker.core.model.dockerfile;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.SimpleTimeZone;

public class DumpToDisk {
    public static void dumpToDisk(Dockerfile dockerfileFilePath, Path pathToFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            Files.deleteIfExists(pathToFile);
            Path file = Files.createFile(pathToFile);

            List<Command> listOfCommand = dockerfileFilePath.getListOfCommand();
            for (Command command : listOfCommand) {
                Method render = DumpToDisk.class.getMethod("render", command.getClass());
                String result = (String) render.invoke(null, command);
                stringBuilder.append(result);

            }

            PrintWriter printWriter = new PrintWriter(file.toFile());
            printWriter.write(stringBuilder.toString());
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static String render(FROMCommand fromCommand) {
        return new StringBuilder().append("FROM ").append(fromCommand.getParent().getDigest()).append("\n").toString();
    }

    public static String render(CMDCommand cmdCommand) {
        StringBuilder stringBuilder = new StringBuilder();


        List<String> body = cmdCommand.getBody();
        for(int i = 0 ; i < body.size() ; i++) {
            String currentstring = body.get(i);

            if(i > 0) {
                stringBuilder.append(", ");
            }

            stringBuilder.append("\"").append(currentstring).append("\"");
        }

        return new StringBuilder().append("CMD [").append(stringBuilder).append("]\n").toString();
    }

    public static String render(ENTRYPointCommand entryPointCommand) {
        StringBuilder stringBuilder = new StringBuilder();


        List<String> body = entryPointCommand.getBody();
        for(int i = 0 ; i < body.size() ; i++) {
            String currentstring = body.get(i);

            if(i > 0) {
                stringBuilder.append(", ");
            }

            stringBuilder.append("\"").append(currentstring).append("\"");
        }

        return new StringBuilder().append("ENTRYPOINT [").append(stringBuilder).append("]\n").toString();
    }

    public static String render(COPYCommand copyCommand) {
        StringBuilder stringBuilder = new StringBuilder();


        List<String> body = copyCommand.getBody();
        for(int i = 0 ; i < body.size() ; i++) {
            String currentstring = body.get(i);

            if(i > 0) {
                stringBuilder.append(", ");
            }

            stringBuilder.append("\"").append(currentstring).append("\"");
        }

        return new StringBuilder().append("COPY [").append(stringBuilder).append("]\n").toString();
    }

    public static String render(ENVCommand envCommand) {
        return new StringBuilder().append("ENV ").append(envCommand.getKey()).append(" ").append(envCommand.getKey()).append("\n").toString();
    }

    public static String render(EXPOSECommand exposeCommand) {
        StringBuilder stringBuilder = new StringBuilder();


        List<String> body = exposeCommand.getBody();
        for(int i = 0 ; i < body.size() ; i++) {
            String currentstring = body.get(i);

            if(i > 0) {
                stringBuilder.append(" ");
            }

            stringBuilder.append(currentstring);
        }

        return new StringBuilder().append("EXPOSE ").append(stringBuilder).append("\n").toString();
    }

    public static String render(ADDCommand addCommand) {
        StringBuilder stringBuilder = new StringBuilder();


        List<String> body = addCommand.getBody();
        for(int i = 0 ; i < body.size() ; i++) {
            String currentstring = body.get(i);

            if(i > 0) {
                stringBuilder.append(", ");
            }

            stringBuilder.append("\"").append(currentstring).append("\"");
        }

        return new StringBuilder().append("ADD [").append(stringBuilder).append("]\n").toString();
    }

    public static String render(MAINTAINERCommand maintainerCommand) {
        StringBuilder stringBuilder = new StringBuilder();


        List<String> body = maintainerCommand.getBody();
        for(int i = 0 ; i < body.size() ; i++) {
            String currentstring = body.get(i);

            if(i > 0) {
                stringBuilder.append(", ");
            }

            stringBuilder.append("\"").append(currentstring).append("\"");
        }

        return new StringBuilder().append("LABEL maintainer ").append(stringBuilder).append("\n").toString();
    }

    public static String render(RUNCommand runCommand) {

        List<ShellCommand> body = runCommand.getBody();

        //  If the first command is another shell, choose json array description
        //  due to parsing behaviour, (see DockerFileParser) if RUN was RUN ["..","..."],
        //  there is only one ShellCommand in the RUN body

        if (!body.isEmpty() && body.size() == 1 && body.get(0).toString().startsWith("[")) {
            return new StringBuilder().append("RUN ").append(body.get(0).getBody().get(0)).append("\n").toString();
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0 ; i < body.size() ; i++) {
            stringBuilder.append(" ");

            ShellCommand currentShellCommand = body.get(i);

            List<String> currentShellCommandBody = currentShellCommand.getBody();
            for(String s : currentShellCommandBody) {
                stringBuilder.append(" ").append(s);
            }

            if(i != body.size() -1) {
                stringBuilder.append("\\\\\n&&");
            }
        }

        return new StringBuilder().append("RUN ").append(stringBuilder).append("\n").toString();
    }

    public static String render(VOLUMECommand volumeCommand) {
        return new StringBuilder().append("VOLUME [\"").append(volumeCommand.getBody()).append("\"]\n").toString();
    }

    public static String render(WORKDIRCommand workdirCommand) {
        return new StringBuilder().append("WORKDIR ").append(workdirCommand.getBody()).append("\n").toString();
    }

    public static String render(USERCommand userCommand) {
        return new StringBuilder().append("USER ").append(userCommand.getBody()).append("\n").toString();
    }
}
