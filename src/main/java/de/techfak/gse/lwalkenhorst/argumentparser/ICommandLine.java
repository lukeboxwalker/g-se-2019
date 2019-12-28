package de.techfak.gse.lwalkenhorst.argumentparser;

public interface ICommandLine {
    boolean hasArgument();
    String getArgument();
    boolean hasOption(String opt);
    String getOptionArg(String opt);
}
