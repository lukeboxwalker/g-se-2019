package de.techfak.gse.lwalkenhorst.argumentparser;

import de.techfak.gse.lwalkenhorst.exceptions.ExitCodeException;
import org.apache.commons.cli.*;

import java.util.Arrays;

public class ArgumentParser {

    private final Options options;

    public ArgumentParser(OptionAdapter... options) {
        this.options = new Options();
        Arrays.stream(options).forEach(this.options::addOption);
    }

    public ICommandLine parse(final String... args) throws ParseException, ExitCodeException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.getArgs().length > 1) {
            throw new ParseException("To many arguments");
        }
        if (hasConflicts(cmd)) {
            throw new OverlappingOptionException("The arguments have overlapping semantics");
        }
        return new CommandLineAdapter(cmd);
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("GSE-Radio", options);
    }

    private boolean hasConflicts(CommandLine cmd) {
        for (Option option : cmd.getOptions()) {
            OptionAdapter optionAdapter = (OptionAdapter) option;
            for (Option opt : cmd.getOptions()) {
                if (((OptionAdapter) opt).getConflictingOptions().contains(optionAdapter)) {
                    return true;
                }
            }
        }
        return false;
    }
}
