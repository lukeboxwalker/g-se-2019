package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.regex.Pattern;

/**
 * Argument that is required by an option.
 * An Argument as a construct follows an option in a commandline
 * to specify the options semantics
 */
public interface Argument {

    /**
     * Argument could be required by an option or
     * could be optional.
     *
     * @return if argument is required for an option
     */
    boolean isRequired();

    /**
     * Specifies a separator for the prefix and the value.
     *
     * @return separator string
     */
    String getValueSeparator();

    /**
     * Specifies a regex pattern.
     * Pattern to match value String required to parse
     * the argument. If pattern matcher failed the argument
     * that is parsed is invalid.
     *
     * @return regex pattern
     */
    Pattern getValuePatternMatcher();

    /**
     * Argument name or prefix.
     * Required to determine the argument that is used in an
     * commandline
     *
     * @return the prefix
     */
    String getPrefix();

    /**
     * Arguments default value.
     * If an argument has a default value this
     * return a non null string object that can be used
     * as a default value, when the argument was not found
     * in commandline
     *
     * @return the default value
     */
    String getDefaultValue();

    /**
     * Argument could have a default value.
     *
     * @return if argument has default value
     */
    boolean hasDefaultValue();
}
