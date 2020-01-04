package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * ArgumentParser to parse arguments.
 */
public class ArgumentParser {

    private static final String ARGUMENT = "Argument ";
    private Pattern longOption = Pattern.compile("^--.*");
    private Pattern shortOption = Pattern.compile("^-.*");

    /**
     * Parsing argument input with specified options.
     * Filtering found options and adds them to commandLine
     * Filtering arguments that belong to an option
     * Can only attach one argument to commandLine (directory)
     *
     * @param options all options possible
     * @param args program arguments
     * @return new parsed CommandLine
     * @throws OverlappingOptionException if there ar option in args that would overlap
     * @throws ParseException if parsing fails
     */
    public ICommandLine parse(final List<IOption> options,
                              final String... args) throws OverlappingOptionException, ParseException {
        CommandLine commandLine = new CommandLine();
        for (int index = 0; index < args.length; index++) {
            String opt = args[index];
            boolean hasLongOption = longOption.matcher(opt).matches();
            boolean hasShortOption = !hasLongOption && shortOption.matcher(opt).matches();
            if (hasLongOption || hasShortOption) {
                opt = hasLongOption ? opt.substring(2) : opt.substring(1);
                boolean optionFound = false;
                for (IOption option : options) {
                    String name = hasLongOption ? option.getName() : option.getShortName();
                    if (name.equals(opt)) {
                        commandLine.addOption(option);
                        final List<IArgument> requiredArguments = option.getRequiredArguments();
                        final int arguments = requiredArguments.size();
                        if (arguments > 0) {
                            final Set<IArgument> foundArgs = new HashSet<>();
                            int deltaIndex = index + 1;
                            while (deltaIndex <= index + arguments && deltaIndex < args.length) {
                                for (IArgument argument : requiredArguments) {
                                    final String arg = args[deltaIndex].substring(2);
                                    if (arg.startsWith(argument.getPrefix())) {
                                        final String[] split = arg.split(argument.getValueSeparator());
                                        if (split.length == 2 && split[0].equals(argument.getPrefix())
                                            && argument.getValuePatternMatcher().matcher(split[1]).matches()) {
                                            commandLine.addOptionArgument(option, argument.getPrefix(), split[1]);
                                            foundArgs.add(argument);
                                            ++index;
                                            break;
                                        } else {
                                            throw new ParseException(ARGUMENT + argument.getPrefix()
                                                + " has invalid structure");
                                        }
                                    }
                                    ++deltaIndex;
                                }
                            }
                            for (IArgument argument : requiredArguments) {
                                if (!foundArgs.contains(argument) && argument.isRequired()) {
                                    if (argument.hasDefaultValue()) {
                                        commandLine.addOptionArgument(option,
                                            argument.getPrefix(), argument.getDefaultValue());
                                    } else {
                                        throw new ParseException(ARGUMENT + argument.getPrefix() + " wasn't found");
                                    }
                                }
                            }
                        }
                        optionFound = true;
                        break;
                    }
                }
                if (!optionFound) {
                    throw new ParseException("Unknown option: " + opt);
                }
            } else {
                commandLine.setArgument(opt);
            }
        }
        return commandLine;
    }
}
