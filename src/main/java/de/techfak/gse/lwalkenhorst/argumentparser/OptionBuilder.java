package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder to build an option object.
 * Using fluent interface design to build option
 */
class OptionBuilder implements Option.Builder {
    private List<String> conflictingOptions;
    private List<IArgument> requiredArguments;
    private String optionName;

    OptionBuilder() {
        this.conflictingOptions = new ArrayList<>();
        this.requiredArguments = new ArrayList<>();
    }

    @Override
    public Option.Builder withArgument(IArgument argument) {
        this.requiredArguments.add(argument);
        return this;
    }

    @Override
    public Option.Builder conflictsOption(String option) {
        this.conflictingOptions.add(option);
        return this;
    }

    @Override
    public Option.Builder withName(String optionName) {
        this.optionName = optionName;
        return this;
    }

    @Override
    public IOption build() {
        if (optionName == null || optionName.isEmpty()) {
            throw new IllegalArgumentException("Option must declare a name");
        } else {
            final Option option = new Option(optionName);
            option.setConflictingOptions(conflictingOptions);
            option.setRequiredArguments(requiredArguments);
            return option;
        }
    }
}
