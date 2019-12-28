package de.techfak.gse.lwalkenhorst.argumentparser;

import org.apache.commons.cli.ParseException;

public interface ICommandLine {
    boolean hasArgument();
    String getArgument();
    boolean hasOption(IOption option);
    String getParsedOptionArg(IOption option) throws ParseException;
}
