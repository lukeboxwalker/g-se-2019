package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Argument that is required by an option.
 * An Argument as a construct follows an option in a commandline
 * to specify the options semantics
 */
public class Argument implements IArgument {

    private final String argumentPrefix;
    private boolean isRequired;
    private String valueSeparator;
    private Pattern valuePatternMatcher;
    private String defaultValue;

    Argument(final String argumentPrefix) {
        this.argumentPrefix = argumentPrefix;
        this.isRequired = true;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String getPrefix() {
        return argumentPrefix;
    }

    public void setValueMatcher(Pattern valuePatternMatcher) {
        this.valuePatternMatcher = valuePatternMatcher;
    }

    @Override
    public Pattern getValuePatternMatcher() {
        return Objects.requireNonNullElse(valuePatternMatcher, Pattern.compile(".*"));
    }

    @Override
    public String getValueSeparator() {
        return Objects.requireNonNullElse(valueSeparator, " ");

    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    @Override
    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public static PrefixBuilder builder() {
        return new ArgumentBuilder();
    }

    /**
     * Builder to build an argument object.
     * Which has to specify a name
     */
    public interface PrefixBuilder {
        /**
         * Prepares the name of the argument.
         * Needs to be set for argument to be instantiated
         *
         * @param argumentName for argument
         * @return builder
         */
        Argument.OptionalsBuilder withName(String argumentName);
    }

    /**
     * Builder to build an argument object.
     * Using fluent interface design to build argument
     */
    public interface OptionalsBuilder {

        /**
         * Prepares isRequired for argument.
         * Default is true
         *
         * @param isRequired to set
         * @return builder
         */
        Argument.OptionalsBuilder isRequired(boolean isRequired);

        /**
         * Prepares valueSeparator for argument.
         * Default is null
         *
         * @param separator to set
         * @return builder
         */
        Argument.OptionalsBuilder withValueSeparator(String separator);

        /**
         * Prepares valuePatternMatcher for argument.
         * Default is null
         *
         * @param valuePatternMatcher to set
         * @return builder
         */
        Argument.OptionalsBuilder withPatternMatcher(Pattern valuePatternMatcher);

        /**
         * Prepares defaultValue for argument.
         * Default is null
         *
         * @param defaultValue to set
         * @return builder
         */
        Argument.OptionalsBuilder withDefaultValue(String defaultValue);

        /**
         * Building the argument.
         *
         * @return new configured argument
         */
        IArgument build();
    }
}
