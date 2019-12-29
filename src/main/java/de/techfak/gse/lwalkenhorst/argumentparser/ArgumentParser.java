package de.techfak.gse.lwalkenhorst.argumentparser;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import org.apache.commons.cli.*;

import java.util.regex.Pattern;

public final class ArgumentParser {

    private static final String PORT_RANGE = "([0-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|[1-8][0-9]{3}"
        + "|9[0-8][0-9]{2}|99[0-8][0-9]|999[0-9]|[1-5][0-9]{4}"
        + "|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

    private final OptionAdapter guiOption;
    private final OptionAdapter clientOption;
    private final OptionAdapter serverOption;

    private final Options options = new Options();

    public ArgumentParser() {
        this.guiOption = new OptionAdapter("gui", "Starting in gui mode.");
        this.clientOption = new OptionAdapter("client", "Starting in client mode.");

        PatternHolder patternHolder = new PatternHolder(
            Pattern.compile("--streaming=" + PORT_RANGE), Pattern.compile(PORT_RANGE));
        this.serverOption = new OptionAdapter("server", patternHolder,
            "Starting in server mode with given argument --streaming=<port> to specify port.");

        options.addOption(guiOption.getOption());
        options.addOption(clientOption.getOption());
        options.addOption(serverOption.getOption());
    }

    public ICommandLine parse(final String... args) throws ParseException, ExitCodeException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.getArgs().length > 1) {
            throw new ParseException("To many arguments");
        }
        if (cmd.hasOption(guiOption.getName()) && cmd.hasOption(clientOption.getName())) {
            throw new OverlappingOptionException("The arguments --gui and --client have overlapping semantics");
        }
        return new CommandLineAdapter(cmd);

    }

    public IOption getGuiOption() {
        return guiOption;
    }

    public IOption getClientOption() {
        return clientOption;
    }

    public IOption getServerOption() {
        return serverOption;
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("GSE-Radio", options);
    }
}
