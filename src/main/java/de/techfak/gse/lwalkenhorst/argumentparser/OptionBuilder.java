package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builder to build an option object.
 * Using fluent interface design to build option
 */
class OptionBuilder implements CommandLineOption.NameBuilder, CommandLineOption.OptionalsBuilder {
    private List<String> conflictingOptions;
    private List<Argument> requiredArguments;
    private String optionName;

    OptionBuilder() {
        this.conflictingOptions = new ArrayList<>();
        this.requiredArguments = new ArrayList<>();
    }

    @Override
    public CommandLineOption.OptionalsBuilder withArgument(Argument argument) {
        this.requiredArguments.add(argument);
        return this;
    }

    @Override
    public CommandLineOption.OptionalsBuilder conflictsOptions(String... option) {
        this.conflictingOptions.addAll(Arrays.asList(option));
        return this;
    }

    @Override
    public CommandLineOption.OptionalsBuilder withName(String optionName) {
        if (optionName.isEmpty()) {
            throw new IllegalArgumentException("Option must declare a name");
        }
        this.optionName = optionName;
        return this;
    }

    @Override
    public Option build() {
        final CommandLineOption commandLineOption = new CommandLineOption(optionName);
        commandLineOption.setConflictingOptions(conflictingOptions);
        commandLineOption.setRequiredArguments(requiredArguments);
        return commandLineOption;
    }
}
