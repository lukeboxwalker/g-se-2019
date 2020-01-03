package de.techfak.gse.lwalkenhorst.argumentparser;

import org.apache.commons.cli.ParseException;

public interface ICommandLine {
    boolean hasArgument();
    String getArgument();
    String getParsedOptionArg(String opt) throws ParseException;

    IOption getOption(String opt);
    boolean hasOption(String opt);
}
