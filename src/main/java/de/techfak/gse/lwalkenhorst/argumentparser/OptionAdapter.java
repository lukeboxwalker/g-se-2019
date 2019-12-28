package de.techfak.gse.lwalkenhorst.argumentparser;

import org.apache.commons.cli.Option;

public class OptionAdapter implements IOption {

    private Option option;
    private PatternHolder patternHolder;

    public OptionAdapter(String option, String description) {
        this.option = new Option(option.substring(0, 1), option, false, description);
    }

    public OptionAdapter(String option, PatternHolder patternHolder, String description) {
        this.option = new Option(option.substring(0, 1), option, true, description);
        this.patternHolder = patternHolder;
    }

    public Option getOption() {
        return option;
    }

    @Override
    public String getName() {
        return option.getOpt();
    }

    @Override
    public PatternHolder getPatternHolder() {
        return patternHolder;
    }
}
