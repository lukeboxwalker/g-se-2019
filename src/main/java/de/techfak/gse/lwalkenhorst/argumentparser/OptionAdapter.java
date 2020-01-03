package de.techfak.gse.lwalkenhorst.argumentparser;

import org.apache.commons.cli.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OptionAdapter extends Option implements IOption {

    private static final long serialVersionUID = 1L;

    private List<IOption> options;
    private Pattern argParsingPattern;
    private Pattern argExtractingPattern;

    public OptionAdapter(Option option) {
        this(option.getArgName(), option.getLongOpt(), option.hasArg(), option.getDescription());
    }

    public OptionAdapter(String longOpt, String description) {
        this(longOpt.substring(0, 1), longOpt, false, description);
    }

    public OptionAdapter(String longOpt, boolean hasArg, String description) {
        this(longOpt.substring(0, 1), longOpt, hasArg, description);
    }

    public OptionAdapter(String opt, String longOpt, boolean hasArg, String description) throws IllegalArgumentException {
        super(opt, longOpt, hasArg, description);
        this.options = new ArrayList<>();
        final Pattern pattern = Pattern.compile("\\*");
        this.argParsingPattern = pattern;
        this.argExtractingPattern = pattern;
    }

    public void addConflictingOption(IOption option) {
        options.add(option);
    }

    public List<IOption> getConflictingOptions() {
        return options;
    }

    @Override
    public void setParsingPattern(Pattern pattern) {
        this.argParsingPattern = pattern;
    }

    @Override
    public void setExtractingPatternPattern(Pattern pattern) {
        this.argExtractingPattern = pattern;
    }

    @Override
    public Pattern getParsingPattern() {
        return argParsingPattern;
    }

    @Override
    public Pattern getExtractingPatternPattern() {
        return argExtractingPattern;
    }


}
