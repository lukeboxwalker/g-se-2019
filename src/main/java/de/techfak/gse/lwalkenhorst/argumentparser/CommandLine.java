package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.*;

/**
 * Commandline that is parsed by a parser.
 */
public class CommandLine implements ICommandLine {

    private final Map<String, IOption> options;
    private final Map<String, Map<String, String>> optionArguments;
    private String argument;

    public CommandLine() {
        this.options = new HashMap<>();
        this.optionArguments = new HashMap<>();
    }

    /**
     * Sets argument that is found by a parser.
     * Only one argument is allowed for a commandline
     *
     * @param argument the argument
     * @throws ParseException if there is more than one argument
     */
    public void setArgument(String argument) throws ParseException {
        if (this.argument == null) {
            this.argument = argument;
        } else {
            throw new ParseException("Too many arguments");
        }

    }

    /**
     * Adding an argument that belongs to an option.
     *
     * @param option the argument belongs to
     * @param argKey the argument prefix
     * @param argValue the argument value
     */
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

    /**
     * Adding an option that was found.
     *
     * @param option found by parsing.
     * @throws OverlappingOptionException if option would overlap an existing option
     */
    public void addOption(IOption option) throws OverlappingOptionException, ParseException {
        for (String optionName : option.getConflictingOptions()) {
            if (options.containsKey(optionName)) {
                throw new OverlappingOptionException("The arguments have overlapping semantics");
            }
        }
        if (options.containsKey(option.getName())) {
            throw new ParseException("Found same option multiple times");
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
