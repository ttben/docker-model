package fr.unice.i3s.sparks.docker.core.model.dockerfile.analyser;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DockerFileParser {

    public static final Pattern FROM_PATTERN = Pattern.compile("[fF][rR][oO][mM](\\s)+[^\\n\\s]+");

    //public static final Pattern ENV_PATTERN = Pattern.compile("[eE][nN][vV](\\s)+[^\\s]+(\\s)*[^\\s]+");
    public static final Pattern ENV_PATTERN = Pattern.compile("[eE][nN][vV](\\s)+[^\\\\]+");
    public static final Pattern ENV_PATTERN_MULTILINE = Pattern.compile("[eE][nN][vV](\\s)+.*(\\\\)+\\s*");
    public static final Pattern ENV_AGAIN_MULTILINE = Pattern.compile(".*(\\\\)+\\s*");
    public static final Pattern ENV_END_MULTILINE = Pattern.compile("[^\\\\]*");

    public static final Pattern CMD_PATTERN = Pattern.compile("[cC][mM][dD](\\s)+(.)+");
    public static final Pattern CMD_PATTERN_MUTLILINE = Pattern.compile("[cC][mM][dD](\\s)+.*(\\\\)+\\s*");
    public static final Pattern CMD_AGAIN_MULTILINE = Pattern.compile(".*(\\\\)+\\s*");
    public static final Pattern CMD_END_MULTILINE = Pattern.compile("[^\\\\]*");

    public static final Pattern COPY_PATTERN = Pattern.compile("[cC][oO][pP][yY](\\s)+(.)+(\\s)+(.)+");
    //public static final Pattern ADD_PATTERN = Pattern.compile("[aA][dD][dD](\\s)+(.)+(\\s)+(.)+");
    public static final Pattern ADD_PATTERN = Pattern.compile("[aA][dD][dD](\\s)+(.)+");

    public static final Pattern ENTRYPOINT_PATTERN = Pattern.compile("[eE][nN][tT][rR][yY][pP][oO][iI][nN][tT](\\s)+(.)+");
    public static final Pattern EXPOSE_PATTERN = Pattern.compile("[eE][xX][pP][oO][sS][eE](\\s)+(.)+");

    public static final Pattern MAINTAINER_PATTERN = Pattern.compile("[mM][aA][iI][nN][tT][aA][iI][nN][eE][rR](\\s)+(.)+");
    public static final Pattern WORKDIR_PATTERN = Pattern.compile("[wW][oO][rR][kK][dD][iI][rR](\\s)+(.)+");
    public static final Pattern VOLUME_PATTERN = Pattern.compile("[vV][oO][lL][uU][mM][eE](\\s)+(.)+");
    public static final Pattern USER_PATTERN = Pattern.compile("[uU][sS][eE][rR](\\s)+(.)+");
    public static final Pattern ONBUILD_PATTERN = Pattern.compile("[oO][nN][bB][uU][iI][lL][dD](\\s)+(.)+");
    public static final Pattern ARG_PATTERN = Pattern.compile("[aA][rR][gG](\\s)+(.)+");

    public static final Pattern LABEL_PATTERN = Pattern.compile("[lL][aA][bB][eE][lL](\\s)+(.)+");
    public static final Pattern LABEL_PATTERN_MUTLILINE = Pattern.compile("[lL][aA][bB][eE][lL](\\s)+.*(\\\\)+\\s*");
    public static final Pattern LABEL_AGAIN_MULTILINE = Pattern.compile(".*(\\\\)+\\s*");
    public static final Pattern LABEL_END_MULTILINE = Pattern.compile("[^\\\\]*");

    public static final Pattern RUN_PATTERN_ONELINE = Pattern.compile("[rR][uU][nN](\\s)+.*[^\\\\]");
    public static final Pattern RUN_PATTERN_MUTLILINE = Pattern.compile("[rR][uU][nN](\\s)+.*(\\\\)+\\s*");
    public static final Pattern RUN_AGAIN_MULTILINE = Pattern.compile(".*(\\\\)+\\s*");
    public static final Pattern RUN_END_MULTILINE = Pattern.compile("[^\\\\]*");

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

            try {
                line = line.replace('\t', ' ');
                line = line.replaceAll("(\\s)+", " ");
                line = line.trim();

                if (line.trim().startsWith("#") | line.trim().isEmpty()) {
                    continue;
                }

                if (FROM_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String body = split[split.length - 1];
                    FROMCommand fromCommand = new FROMCommand(new ImageID(body));
                    result.addCommand(fromCommand);
                    continue;
                }

                if (WORKDIR_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String body = split[split.length - 1];
                    WORKDIRCommand workdirCommand = new WORKDIRCommand(body);
                    result.addCommand(workdirCommand);
                    continue;
                }

                if (VOLUME_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String body = split[split.length - 1];
                    VOLUMECommand volumeCommand = new VOLUMECommand(body);
                    result.addCommand(volumeCommand);
                    continue;
                }

                if (USER_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String body = split[split.length - 1];
                    USERCommand userCommand = new USERCommand(body);
                    result.addCommand(userCommand);
                    continue;
                }

                if (MAINTAINER_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);
                    MAINTAINERCommand maintainerCommand = new MAINTAINERCommand(body);
                    result.addCommand(maintainerCommand);
                    continue;
                }

                if (ENV_PATTERN.matcher(line).matches()) {
                    List<Command> envCommand = buildENVCommand(line);
                    result.addCommands(envCommand);
                    continue;
                }


                if (ENV_PATTERN_MULTILINE.matcher(line).matches()) {
                    List<EnvKeyValue> keyValuePairs = buildMultiLinesEnvCommand(line, true);
                    line = stringListIterator.next();

                    while (ENV_AGAIN_MULTILINE.matcher(line).matches() || line.trim().isEmpty() || line.trim().startsWith("#")) {
                        if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                            line = stringListIterator.next();
                            continue;
                        }
                        keyValuePairs.addAll(buildMultiLinesEnvCommand(line, false));
                        line = stringListIterator.next();
                    }

                    if (ENV_END_MULTILINE.matcher(line).matches()) {
                        keyValuePairs.addAll(buildMultiLinesEnvCommand(line, false));
                    }
                    result.addCommand(new ENVCommand(keyValuePairs));
                    continue;
                }

                /*
                if (CMD_PATTERN_MUTLILINE.matcher(line).matches()) {
                    List<String> body = new ArrayList<>();
                    body.add(line);

                    line = stringListIterator.next();

                    while (CMD_AGAIN_MULTILINE.matcher(line).matches() || line.trim().startsWith("#")) {
                        body.add(line);
                        line = stringListIterator.next();
                    }

                    if (CMD_END_MULTILINE.matcher(line).matches()) {
                        body.add(line);
                    }
                    result.addCommand(new CMDCommand((String[]) body.toArray()));
                    continue;
                }
                */

                //  Fixme/todo must be handled as RUNCommand (replace ShellCommand?)
                if (CMD_PATTERN_MUTLILINE.matcher(line).matches()) {
                    String body = line;

                    while (CMD_AGAIN_MULTILINE.matcher(line).matches() || line.trim().isEmpty() || line.trim().startsWith("#")) {
                        if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                            line = stringListIterator.next();
                            continue;
                        }
                        body += " " + line;
                        line = stringListIterator.next();
                    }

                    if (CMD_END_MULTILINE.matcher(line).matches()) {
                        body += " " + line;
                    }
                    result.addCommand(new CMDCommand(body));
                    continue;
                }


                if (CMD_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    CMDCommand cmdCommand = new CMDCommand(body);
                    result.addCommand(cmdCommand);
                    continue;
                }

                if (ADD_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    ADDCommand addCommand = new ADDCommand(body);
                    result.addCommand(addCommand);
                    continue;
                }

                if (COPY_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    COPYCommand copyCommand = new COPYCommand(body);
                    result.addCommand(copyCommand);
                    continue;
                }

                if (ENTRYPOINT_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    ENTRYPointCommand entryPointCommand = new ENTRYPointCommand(body);
                    result.addCommand(entryPointCommand);
                    continue;
                }

                if (EXPOSE_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = (String[]) Arrays.copyOfRange(split, 1, split.length);

                    EXPOSECommand exposeCommand = new EXPOSECommand(body);
                    result.addCommand(exposeCommand);
                    continue;
                }

                if (ONBUILD_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = Arrays.copyOfRange(split, 1, split.length);

                    ONBUILDCommand entryPointCommand = new ONBUILDCommand(body);
                    result.addCommand(entryPointCommand);
                    continue;
                }

                if (ARG_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = Arrays.copyOfRange(split, 1, split.length);

                    ARGCommand entryPointCommand = new ARGCommand(body);
                    result.addCommand(entryPointCommand);
                    continue;
                }


                if (LABEL_PATTERN_MUTLILINE.matcher(line).matches()) {
                    String body = line;

                    while (LABEL_AGAIN_MULTILINE.matcher(line).matches() || line.trim().isEmpty() || line.trim().startsWith("#")) {
                        if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                            line = stringListIterator.next();
                            continue;
                        }
                        body += " " + line;
                        line = stringListIterator.next();
                    }

                    if (LABEL_END_MULTILINE.matcher(line).matches()) {
                        body += " " + line;
                    }
                    result.addCommand(new LABELCommand(body));
                    continue;
                }


                if (LABEL_PATTERN.matcher(line).matches()) {
                    String[] split = line.split(" ");

                    String[] body = Arrays.copyOfRange(split, 1, split.length);

                    LABELCommand entryPointCommand = new LABELCommand(body);
                    result.addCommand(entryPointCommand);
                    continue;
                }

                if (RUN_PATTERN_MUTLILINE.matcher(line).matches()) {
                    List<ShellCommand> shellCommands = buildRunCommand(line, true);
                    line = stringListIterator.next();

                    while (RUN_AGAIN_MULTILINE.matcher(line).matches() || line.trim().isEmpty() || line.trim().startsWith("#")) {
                        if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                            line = stringListIterator.next();
                            continue;
                        }
                        shellCommands.addAll(buildRunCommand(line, false));
                        line = stringListIterator.next();
                    }

                    if (RUN_END_MULTILINE.matcher(line).matches()) {
                        shellCommands.addAll(buildRunCommand(line, false));
                    }
                    result.addCommand(new RUNCommand(shellCommands));
                    continue;
                }

                if (RUN_PATTERN_ONELINE.matcher(line).matches()) {
                    List<ShellCommand> shellCommands = buildRunCommand(line, true);
                    RUNCommand runCommand = new RUNCommand(shellCommands);
                    result.addCommand(runCommand);
                    continue;
                }


                result.addCommand(new NonParsedCommand(line));
            } catch (NoSuchElementException | IndexOutOfBoundsException e) {
                e.printStackTrace();
                //System.err.println("Parse error on file:" + file.getAbsolutePath());
                //result.addCommand(new NonParsedCommand(line));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

        char[] charArray = line.toCharArray();
        int index = 0;


        ENVAutomata envAutomata = new ENVAutomata();
        envAutomata.handle(charArray, 0);
        envKeyValues = envAutomata.getEnvKeyValues();

        return envKeyValues;
    }

    private static void extractKeyValue( char[] charArray, int index) {
        List<EnvKeyValue> envKeyValues = new ArrayList<>();
        while(index < charArray.length) {

            List<Character> key = new ArrayList<>();
            List<Character> value = new ArrayList<>();

            while (charArray[index] != '=' && (Character.isLetter(charArray[index]) || Character.isDigit(charArray[index]))) {
                key.add(charArray[index]);
                index++;
            }

            index++;

            if (charArray[index] == '"') {

            } else {
                while (charArray[index] != '=' && (Character.isLetter(charArray[index]) || Character.isDigit(charArray[index]))) {
                    value.add(charArray[index]);
                    if(index != charArray.length-1) {
                        index++;
                    }
                }

                envKeyValues.add(new EnvKeyValue(key.toString(), value.toString()));
                index++;
            }
        }

        System.out.println(envKeyValues);
    }

    private static List<Command> buildENVCommand(String line) {
        line = line.trim();


        if (line.toLowerCase().startsWith("env")) {
            //  Delete env command
            line = line.substring(3, line.length());
            line = line.trim();
        }

        if (line.endsWith("\\")) {
            //  Delete '\' suffix
            line = line.substring(0, line.length() - 1);
            line = line.trim();
        }


        String[] body = line.split(" ");

        String key, value = "";

        List<Command> result = new ArrayList<>();

        key = body[0];
        for(int i = 1 ; i < body.length ; i++) {
            if(i != 1) {
                value += " ";
            }
            value += body[i];
        }


        result.add(new ENVCommand(Arrays.asList(new EnvKeyValue(key, value))));

        return result;
    }

    private static List<ShellCommand> buildRunCommand(String line, boolean isFirstLineOfRun) {
        line = line.trim();
        String[] split = line.split("&&");

        if (isFirstLineOfRun) {
            split[0] = split[0].substring(4);
        }

        List<ShellCommand> shellCommands = new ArrayList<>();
        for (String s : split) {
            s = s.trim();
            ShellCommand shellCommand = new ShellCommand(s.split(" "));
            shellCommands.add(shellCommand);
        }

        return shellCommands;
    }

    public static void main(String[] args) {
        List<EnvKeyValue> envKeyValues = DockerFileParser.buildMultiLinesEnvCommand("ENV a=aq2 b=s\\", true);
        System.out.println(envKeyValues);
    }
}