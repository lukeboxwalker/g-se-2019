package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Option that could be used in a commandline.
 */
public class CommandLineOption implements Option {

    private final String optionName;
    private final String shortName;
    private List<String> conflictingOptions;
    private List<Argument> requiredArguments;

    /**
     * Creates a new Option with given name.
     * Option always requires a name to specify its
     * long name and short name (first letter of long name)
     *
     * @param optionName to set name of option
     */
    CommandLineOption(final String optionName) {
        this.optionName = optionName;
        this.shortName = optionName.substring(0, 1);
        this.conflictingOptions = new ArrayList<>();
        this.requiredArguments = new ArrayList<>();
    }

    public void setConflictingOptions(List<String> conflictingOptions) {
        this.conflictingOptions = conflictingOptions;
    }

    public void setRequiredArguments(List<Argument> requiredArguments) {
        this.requiredArguments = requiredArguments;
    }

    @Override
    public String getName() {
        return optionName;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void addRequiredArguments(Argument argument) {
        requiredArguments.add(argument);
    }

    @Override
    public List<Argument> getRequiredArguments() {
        return requiredArguments;
    }

    @Override
    public void addConflictingOption(String option) {
        conflictingOptions.add(option);
    }

    @Override
    public List<String> getConflictingOptions() {
        return conflictingOptions;
    }

    public static NameBuilder builder() {
        return new OptionBuilder();
    }

    /**
     * Builder to build an option object.
     * Which has to specify a name
     */
    public interface NameBuilder {
        /**
         * Prepares the name of the option.
         * Needs to be set for option to be instantiated
         *
         * @param optionName for option
         * @return builder
         */
        CommandLineOption.OptionalsBuilder withName(String optionName);
    }

    /**
     * Builder to build an option object.
     * Using fluent interface design to build option
     */
    public interface OptionalsBuilder {

        /**
         * Prepares to add a new argument to the option.
         *
         * @param argument to add
         * @return builder
         */
        CommandLineOption.OptionalsBuilder withArgument(Argument argument);

        /**
         * Prepares to add a conflicting option.
         *
         * @param option that conflicts
         * @return builder
         */
        CommandLineOption.OptionalsBuilder conflictsOptions(String... option);

        /**
         * Building the option object.
         * Using all configurations set during
         * the building process.
         *
         * @return new option
         */
        Option build();
    }
}
