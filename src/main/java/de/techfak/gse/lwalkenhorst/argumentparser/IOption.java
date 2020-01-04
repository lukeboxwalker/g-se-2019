package de.techfak.gse.lwalkenhorst.argumentparser;


import java.util.List;

public interface IOption {
    List<String> getConflictingOptions();
    void addConflictingOption(String option);
    String getName();
    String getShortName();
    List<IArgument> getRequiredArguments();
    void addRequiredArguments(IArgument argument);

}
