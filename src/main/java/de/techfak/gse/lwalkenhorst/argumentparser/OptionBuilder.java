package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder to build an option object.
 * Using fluent interface design to build option
 */
class OptionBuilder implements Option.NameBuilder, Option.OptionalsBuilder {
    private List<String> conflictingOptions;
    private List<IArgument> requiredArguments;
    private String optionName;

    OptionBuilder() {
        this.conflictingOptions = new ArrayList<>();
        this.requiredArguments = new ArrayList<>();
    }

    @Override
    public Option.OptionalsBuilder withArgument(IArgument argument) {
        this.requiredArguments.add(argument);
        return this;
    }

    @Override
    public Option.OptionalsBuilder conflictsOption(String option) {
        this.conflictingOptions.add(option);
        return this;
    }

    @Override
    public Option.OptionalsBuilder withName(String optionName) {
        if (optionName.isEmpty()) {
            throw new IllegalArgumentException("Option must declare a name");
        }
        this.optionName = optionName;
        return this;
    }

    @Override
    public IOption build() {
        final Option option = new Option(optionName);
        option.setConflictingOptions(conflictingOptions);
        option.setRequiredArguments(requiredArguments);
        return option;
    }
}
