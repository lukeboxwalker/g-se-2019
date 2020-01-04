package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IArgument {
    boolean isRequired();
    String getValueSeparator();
    Pattern getValuePatternMatcher();
    String getPrefix();
    String getDefaultValue();
    boolean hasDefaultValue();
}
