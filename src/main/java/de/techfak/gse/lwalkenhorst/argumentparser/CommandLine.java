package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.*;

public class CommandLine implements ICommandLine {

    private final Map<String, IOption> options;
    private final Map<String, Map<String, String>> optionArguments;
    private String argument;

    public CommandLine() {
        this.options = new HashMap<>();
        this.optionArguments = new HashMap<>();
    }

    public void setArgument(String argument) throws ParseException {
        if (this.argument == null) {
            this.argument = argument;
        } else {
            throw new ParseException("Too many arguments");
        }

    }

    public void addOptionArgument(IOption option, String argKey, String argValue) {
        final String optionName = option.getName();
        if (optionArguments.containsKey(optionName)) {
            Map<String, String> arguments = optionArguments.get(optionName);
            arguments.put(argKey, argValue);
        } else {
            Map<String, String> arguments = new HashMap<>();
            arguments.put(argKey, argValue);
            optionArguments.put(optionName, arguments);
        }
    }

    public void addOption(IOption option) throws OverlappingOptionException {
        for (String optionName : option.getConflictingOptions()) {
            if (options.containsKey(optionName)) {
                throw new OverlappingOptionException("The arguments have overlapping semantics");
            }
        }
        this.options.put(option.getName(), option);
    }

    @Override
    public boolean hasArgument() {
        return argument != null;
    }

    @Override
    public String getArgument() {
        return argument;
    }

    @Override
    public String getOptionArg(String opt, String arg) {
        return hasOptionArg(opt, arg) ? optionArguments.get(opt).get(arg) : "";
    }

    @Override
    public boolean hasOptionArg(String opt, String arg) {
        return (optionArguments.containsKey(opt) && optionArguments.get(opt).containsKey(arg));
    }

    @Override
    public Collection<IOption> getOptions() {
        return options.values();
    }

    @Override
    public Collection<Map<String, String>> getOptionArgs() {
        return optionArguments.values();
    }

    @Override
    public IOption getOption(String opt) {
        return hasOption(opt) ? options.get(opt) : null;
    }

    @Override
    public boolean hasOption(String opt) {
        return options.containsKey(opt);
    }
}
