package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.Collection;
import java.util.Map;

/**
 * Commandline that is parsed by a parser.
 */
public interface ICommandLine {

    /**
     * Commandline could have an argument.
     *
     * @return if the commandline has an argument
     */
    boolean hasArgument();

    /**
     * @return the argument.
     */
    String getArgument();

    /**
     * Gets the value associated with given argument prefix.
     * Commandline stores argument values for option
     * and its argument
     *
     * @param opt the option name
     * @param arg the argument name
     * @return the argument value
     */
    String getOptionArg(String opt, String arg);

    /**
     * Check if option has argument in commandline.
     *
     * @param opt the option name
     * @param arg the argument name
     * @return if argument was found
     */
    boolean hasOptionArg(String opt, String arg);

    /**
     * Getting all options that was found.
     *
     * @return the options
     */
    Collection<IOption> getOptions();

    /**
     * Getting all arguments that are associated with options.
     *
     * @return all arguments.
     */
    Collection<Map<String, String>> getOptionArgs();

    /**
     * Getting the option object by its option name.
     *
     * @param opt the option name
     * @return the option object
     */
    IOption getOption(String opt);

    /**
     * Checks if option was found.
     *
     * @param opt the option name
     * @return if option is present
     */
    boolean hasOption(String opt);
}
