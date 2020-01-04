package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Option that could be used in a commandline.
 */
public class Option implements IOption {

    private final String optionName;
    private final String shortName;
    private List<String> conflictingOptions;
    private List<IArgument> requiredArguments;

    /**
     * Creates a new Option with given name.
     * Option always requires a name to specify its
     * long name and short name (first letter of long name)
     *
     * @param optionName to set name of option
     */
    public Option(final String optionName) {
        this.optionName = optionName;
        this.shortName = optionName.substring(0, 1);
        this.conflictingOptions = new ArrayList<>();
        this.requiredArguments = new ArrayList<>();
    }

    public void setConflictingOptions(List<String> conflictingOptions) {
        this.conflictingOptions = conflictingOptions;
    }

    public void setRequiredArguments(List<IArgument> requiredArguments) {
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
    public void addRequiredArguments(IArgument argument) {
        requiredArguments.add(argument);
    }

    @Override
    public List<IArgument> getRequiredArguments() {
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

    public static Builder builder() {
        return new OptionBuilder();
    }

    /**
     * Builder to build an option object.
     * Using fluent interface design to build option
     */
    public interface Builder {

        /**
         * Prepares to add a new argument to the option.
         *
         * @param argument to add
         * @return builder
         */
        Option.Builder withArgument(IArgument argument);

        /**
         * Prepares to add a conflicting option.
         *
         * @param option that conflicts
         * @return builder
         */
        Option.Builder conflictsOption(String option);

        /**
         * Prepares the name of the option.
         * Needs to be set for option to be instantiated
         *
         * @param optionName for option
         * @return builder
         */
        Option.Builder withName(String optionName);

        /**
         * Building the option object.
         * Using all configurations set during
         * the building process.
         *
         * @return new option
         */
        IOption build();
    }
}
