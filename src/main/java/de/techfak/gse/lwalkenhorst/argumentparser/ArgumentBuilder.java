package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.regex.Pattern;

/**
 * Builder to build an argument object.
 * Using fluent interface design to build argument
 */
class ArgumentBuilder implements Argument.OptionalsBuilder, Argument.PrefixBuilder {

    private String argumentName;
    private boolean isRequired;
    private String valueSeparator;
    private Pattern valuePatternMatcher;
    private String defaultValue;

    ArgumentBuilder() {
        this.isRequired = true;
    }

    @Override
    public Argument.OptionalsBuilder withName(String argumentName) {
        this.argumentName = argumentName;
        return this;
    }

    @Override
    public Argument.OptionalsBuilder isRequired(boolean isRequired) {
        this.isRequired = isRequired;
        return this;
    }

    @Override
    public Argument.OptionalsBuilder withValueSeparator(String separator) {
        this.valueSeparator = separator;
        return this;
    }

    @Override
    public Argument.OptionalsBuilder withPatternMatcher(Pattern valuePatternMatcher) {
        this.valuePatternMatcher = valuePatternMatcher;
        return this;
    }

    @Override
    public Argument.OptionalsBuilder withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public IArgument build() {
        if (argumentName == null || argumentName.isEmpty()) {
            throw new IllegalArgumentException("Argument must declare a name");
        } else {
            Argument argument = new Argument(argumentName);
            argument.setRequired(isRequired);
            argument.setDefaultValue(defaultValue);
            argument.setValueMatcher(valuePatternMatcher);
            argument.setValueSeparator(valueSeparator);
            return argument;
        }
    }
}
