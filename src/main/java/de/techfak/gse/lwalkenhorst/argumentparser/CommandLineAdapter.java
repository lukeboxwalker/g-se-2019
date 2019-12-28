package de.techfak.gse.lwalkenhorst.argumentparser;

import org.apache.commons.cli.CommandLine;

public class CommandLineAdapter implements ICommandLine {

    private CommandLine commandLine;

    public CommandLineAdapter(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public boolean hasArgument() {
        return commandLine.getArgs().length == 1;
    }

    @Override
    public String getArgument() {
        return commandLine.getArgs()[0];
    }

    @Override
    public boolean hasOption(String opt) {
       return commandLine.hasOption(opt);
    }

    @Override
    public String getOptionArg(String opt) {
        return commandLine.getOptionValue(opt);
    }
}
