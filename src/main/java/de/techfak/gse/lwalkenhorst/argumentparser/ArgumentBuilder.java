package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.regex.Pattern;

/**
 * Builder to build an argument object.
 * Using fluent interface design to build argument
 */
class ArgumentBuilder implements CommandLineArgument.OptionalsBuilder, CommandLineArgument.PrefixBuilder {

    private String argumentName;
    private boolean isRequired;
    private String valueSeparator;
    private Pattern valuePatternMatcher;
    private String defaultValue;

    ArgumentBuilder() {
        this.isRequired = true;
    }

    @Override
    public CommandLineArgument.OptionalsBuilder withName(String argumentName) {
        this.argumentName = argumentName;
        return this;
    }

    @Override
    public CommandLineArgument.OptionalsBuilder isRequired(boolean isRequired) {
        this.isRequired = isRequired;
        return this;
    }

    @Override
    public CommandLineArgument.OptionalsBuilder withValueSeparator(String separator) {
        this.valueSeparator = separator;
        return this;
    }

    @Override
    public CommandLineArgument.OptionalsBuilder withPatternMatcher(Pattern valuePatternMatcher) {
        this.valuePatternMatcher = valuePatternMatcher;
        return this;
    }

    @Override
    public CommandLineArgument.OptionalsBuilder withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public Argument build() {
        if (argumentName == null || argumentName.isEmpty()) {
            throw new IllegalArgumentException("Argument must declare a name");
        } else {
            CommandLineArgument commandLineArgument = new CommandLineArgument(argumentName);
            commandLineArgument.setRequired(isRequired);
            commandLineArgument.setDefaultValue(defaultValue);
            commandLineArgument.setValueMatcher(valuePatternMatcher);
            commandLineArgument.setValueSeparator(valueSeparator);
            return commandLineArgument;
        }
    }
}
