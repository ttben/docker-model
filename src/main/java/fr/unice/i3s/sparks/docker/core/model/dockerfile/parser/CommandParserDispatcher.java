package fr.unice.i3s.sparks.docker.core.model.dockerfile.parser;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.commands.*;

import java.util.regex.Pattern;

public class CommandParserDispatcher {
    private static final Pattern FROM_PATTERN = Pattern.compile("[fF][rR][oO][mM](\\s)+[^\\n\\s]+");

    private static final Pattern ENV_PATTERN = Pattern.compile("[eE][nN][vV](\\s)+[^\\\\]+");
    private static final Pattern ENV_PATTERN_MULTILINE = Pattern.compile("[eE][nN][vV](\\s)+.*(\\\\)+\\s*");

    private static final Pattern CMD_PATTERN = Pattern.compile("[cC][mM][dD](\\s)+(.)+");
    private static final Pattern CMD_PATTERN_MUTLILINE = Pattern.compile("[cC][mM][dD](\\s)+.*(\\\\)+\\s*");

    private static final Pattern COPY_PATTERN = Pattern.compile("[cC][oO][pP][yY](\\s)+(.)+(\\s)+(.)+");
    private static final Pattern ADD_PATTERN = Pattern.compile("[aA][dD][dD](\\s)+(.)+");

    private static final Pattern ENTRYPOINT_PATTERN = Pattern.compile("[eE][nN][tT][rR][yY][pP][oO][iI][nN][tT](\\s)+(.)+");
    private static final Pattern EXPOSE_PATTERN = Pattern.compile("[eE][xX][pP][oO][sS][eE](\\s)+(.)+");

    private static final Pattern MAINTAINER_PATTERN = Pattern.compile("[mM][aA][iI][nN][tT][aA][iI][nN][eE][rR](\\s)+(.)+");
    private static final Pattern WORKDIR_PATTERN = Pattern.compile("[wW][oO][rR][kK][dD][iI][rR](\\s)+(.)+");
    private static final Pattern VOLUME_PATTERN = Pattern.compile("[vV][oO][lL][uU][mM][eE](\\s)+(.)+");
    private static final Pattern USER_PATTERN = Pattern.compile("[uU][sS][eE][rR](\\s)+(.)+");
    private static final Pattern ONBUILD_PATTERN = Pattern.compile("[oO][nN][bB][uU][iI][lL][dD](\\s)+(.)+");
    private static final Pattern ARG_PATTERN = Pattern.compile("[aA][rR][gG](\\s)+(.)+");

    private static final Pattern LABEL_PATTERN = Pattern.compile("[lL][aA][bB][eE][lL](\\s)+(.)+");
    private static final Pattern LABEL_PATTERN_MUTLILINE = Pattern.compile("[lL][aA][bB][eE][lL](\\s)+.*(\\\\)+\\s*");

    private static final Pattern RUN_PATTERN_ONELINE = Pattern.compile("[rR][uU][nN](\\s)+.*[^\\\\]");
    private static final Pattern RUN_PATTERN_MUTLILINE = Pattern.compile("[rR][uU][nN](\\s)+.*(\\\\)+\\s*");

    public static CommandParser dispatch(String line) {
        if (FROM_PATTERN.matcher(line).matches()) {
            return new FromCommandParser();
        }

        if (WORKDIR_PATTERN.matcher(line).matches()) {
            return new WorkdirCommandParser();
        }

        if (VOLUME_PATTERN.matcher(line).matches()) {
            return new VolumeCommandParser();
        }

        if (USER_PATTERN.matcher(line).matches()) {
            return new UserCommandParser();
        }

        if (MAINTAINER_PATTERN.matcher(line).matches()) {
            return new MaintainerCommandParser();
        }

        if (ENV_PATTERN.matcher(line).matches()) {
            return new EnvOneLineCommandParser();
        }


        if (ENV_PATTERN_MULTILINE.matcher(line).matches()) {
            return new EnvMultiLineCommandParser();
        }

        if (CMD_PATTERN_MUTLILINE.matcher(line).matches()) {
            return new CmdMultiLineCommandParser();
        }


        if (CMD_PATTERN.matcher(line).matches()) {
            return new CmdOneLineCommandParser();
        }

        if (ADD_PATTERN.matcher(line).matches()) {
            return new AddCommandParser();
        }

        if (COPY_PATTERN.matcher(line).matches()) {
            return new CopyCommandParser();
        }

        if (ENTRYPOINT_PATTERN.matcher(line).matches()) {
            return new EntrypointCommandParser();
        }

        if (EXPOSE_PATTERN.matcher(line).matches()) {
            return new ExposeCommandParser();
        }

        if (ONBUILD_PATTERN.matcher(line).matches()) {
            return new OnBuildCommandParser();
        }

        if (ARG_PATTERN.matcher(line).matches()) {
            return new ArgCommandParser();
        }


        if (LABEL_PATTERN_MUTLILINE.matcher(line).matches()) {
            return new LabelMultiLineCommandParser();
        }


        if (LABEL_PATTERN.matcher(line).matches()) {
            return new LabelOneLineCommandParser();
        }

        if (RUN_PATTERN_MUTLILINE.matcher(line).matches()) {
            return new RunMultiLineCommandParser();
        }

        if (RUN_PATTERN_ONELINE.matcher(line).matches()) {
            return new RunOneLineCommandParser();
        }

        return new NonParsedCommandParser();
    }
}
