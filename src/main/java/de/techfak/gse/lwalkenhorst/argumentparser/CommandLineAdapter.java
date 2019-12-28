package de.techfak.gse.lwalkenhorst.argumentparser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.util.regex.Matcher;

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
    public boolean hasOption(IOption option) {
       return commandLine.hasOption(option.getName());
    }

    @Override
    public String getParsedOptionArg(IOption option) throws ParseException {
        return parse(option, commandLine.getOptionValue(option.getName()));
    }

    private String parse(final IOption option, final String arg) throws ParseException {
        PatternHolder patternHolder = option.getPatternHolder();
        Matcher matcher = patternHolder.getFilterPattern().matcher(arg);
        if (patternHolder.getParsePattern().matcher(arg).matches() && matcher.find()) {
            return matcher.group();
        } else {
            throw new ParseException("Could not parse argument");
        }
    }
}
