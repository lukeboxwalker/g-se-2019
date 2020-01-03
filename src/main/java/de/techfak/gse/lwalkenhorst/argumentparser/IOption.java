package de.techfak.gse.lwalkenhorst.argumentparser;


import java.util.List;
import java.util.regex.Pattern;

public interface IOption {
    List<IOption> getConflictingOptions();
    void addConflictingOption(IOption option);

    void setParsingPattern(Pattern pattern);
    void setExtractingPatternPattern(Pattern pattern);
    Pattern getParsingPattern();
    Pattern getExtractingPatternPattern();
}
