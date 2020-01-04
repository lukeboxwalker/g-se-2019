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

    private Option(final Builder builder) {
        this(builder.optionName);
        this.conflictingOptions = builder.conflictingOptions;
        this.requiredArguments = builder.requiredArguments;
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
        return new Builder();
    }

    /**
     * Builder to build an option object.
     * Using fluent interface design to build option
     */
    public static final class Builder {
        private List<String> conflictingOptions;
        private List<IArgument> requiredArguments;
        private String optionName;

        private Builder() {
            this.conflictingOptions = new ArrayList<>();
            this.requiredArguments = new ArrayList<>();
        }

        public Builder withArgument(IArgument argument) {
            this.requiredArguments.add(argument);
            return this;
        }

        public Builder conflictsOption(String option) {
            this.conflictingOptions.add(option);
            return this;
        }

        public Builder withName(String optionName) {
            this.optionName = optionName;
            return this;
        }

        /**
         * Building the option.
         * Checks if required optionName is set
         *
         * @return new configured option
         */
        public IOption build() {
            if (optionName == null || optionName.isEmpty()) {
                throw new IllegalArgumentException("Option must declare a name");
            } else {
                return new Option(this);
            }
        }
    }
}
