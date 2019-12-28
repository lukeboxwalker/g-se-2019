package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.regex.Pattern;

public class PatternHolder {

    private Pattern parsePattern;
    private Pattern filterPattern;

    public PatternHolder(Pattern parsePattern, Pattern filterPattern) {
        this.parsePattern = parsePattern;
        this.filterPattern = filterPattern;
    }

    public Pattern getParsePattern() {
        return parsePattern;
    }

    public Pattern getFilterPattern() {
        return filterPattern;
    }
}
