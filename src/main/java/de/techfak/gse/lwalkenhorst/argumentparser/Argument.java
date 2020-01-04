package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Argument implements IArgument {

    private final String argumentPrefix;
    private boolean isRequired;
    private String valueSeparator;
    private Pattern valuePatternMatcher;
    private String defaultValue;

    public Argument(final String argumentPrefix) {
        this.argumentPrefix = argumentPrefix;
    }

    private Argument(final Builder builder) {
        this(builder.argumentName);
        this.isRequired = builder.isRequired;
        this.valueSeparator = builder.valueSeparator;
        this.valuePatternMatcher = builder.valuePatternMatcher;
        this.defaultValue = builder.defaultValue;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String argumentName;
        private boolean isRequired;
        private String valueSeparator;
        private Pattern valuePatternMatcher;
        private String defaultValue;

        private Builder() {
        }

        public Builder withName(String argumentName) {
            this.argumentName = argumentName;
            return this;
        }

        public Builder isRequired(boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        public Builder withValueSeparator(String separator) {
            this.valueSeparator = separator;
            return this;
        }

        public Builder withPatternMatcher(Pattern valuePatternMatcher) {
            this.valuePatternMatcher = valuePatternMatcher;
            return this;
        }

        public Builder withDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public IArgument build() {
            if (argumentName == null || argumentName.isEmpty()) {
                throw new IllegalArgumentException("Argument must declare a name");
            } else {
                return new Argument(this);
            }
        }
    }
}
