package de.techfak.gse.lwalkenhorst.argumentparser;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface ICommandLine {
    boolean hasArgument();
    String getArgument();

    String getOptionArg(String opt, String arg);
    boolean hasOptionArg(String opt, String arg);

    Collection<IOption> getOptions();
    Collection<Map<String, String>> getOptionArgs();

    IOption getOption(String opt);
    boolean hasOption(String opt);
}
