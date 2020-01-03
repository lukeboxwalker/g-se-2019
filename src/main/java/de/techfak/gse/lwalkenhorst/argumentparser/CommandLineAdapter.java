package de.techfak.gse.lwalkenhorst.argumentparser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
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
    public boolean hasOption(String opt) {
       return commandLine.hasOption(opt);
    }

    @Override
    public IOption getOption(String opt) {
        for (Option option : commandLine.getOptions()) {
            if (option.getLongOpt().equals(opt)) {
                return (IOption) option;
            }
        }
        return null;
    }

    @Override
    public String getParsedOptionArg(String opt) throws ParseException {
        return parse(getOption(opt), commandLine.getOptionValue(opt));
    }

    private String parse(final IOption option, final String arg) throws ParseException {
        if (option == null || arg.isEmpty()) {
            return "";
        }
        Matcher matcher = option.getExtractingPatternPattern().matcher(arg);
        if (option.getParsingPattern().matcher(arg).matches() && matcher.find()) {
            return matcher.group();
        } else {
            throw new ParseException("Could not parse argument");
        }
    }
}
