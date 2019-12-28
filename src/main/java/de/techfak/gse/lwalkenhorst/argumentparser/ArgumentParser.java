package de.techfak.gse.lwalkenhorst.argumentparser;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import org.apache.commons.cli.*;

public final class ArgumentParser {

    public static final String GUI_OPTION = "g";
    public static final String CLIENT_OPTION = "c";
    private final Options options = new Options();


    public ArgumentParser() {
        options.addOption(GUI_OPTION, "gui", false, "Starting in gui mode.");
        options.addOption(CLIENT_OPTION, "client", false, "Starting in client mode.");
    }

    public ICommandLine parse(final String... args) throws ParseException, ExitCodeException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.getArgs().length > 1) {
            throw new ParseException("To many Arguments");
        }
        if (cmd.hasOption(GUI_OPTION) && cmd.hasOption(CLIENT_OPTION)) {
            throw new OverlappingOptionException("The Arguments --gui and --client have overlapping semantics");
        }
        return new CommandLineAdapter(cmd);

    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("GSE-Radio", options);
    }
}
