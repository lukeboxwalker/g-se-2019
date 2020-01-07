package de.techfak.gse.lwalkenhorst.argumentparser;


import java.util.List;

/**
 * Option that could be used in a commandline.
 */
public interface Option {

    /**
     * Option can conflict other options.
     * Conflicting options are stored in a list
     *
     * @return list of conflicting option names
     */
    List<String> getConflictingOptions();

    /**
     * Adds a conflicting option.
     * Identifies options by there names
     *
     * @param option the conflicting option name
     */
    void addConflictingOption(String option);

    /**
     * Each option specifies a name.
     *
     * @return option name
     */
    String getName();

    /**
     * Each option specifies a short name.
     * Default short name is the first letter of the name
     *
     * @return option short name
     */
    String getShortName();

    /**
     * Option may have arguments.
     * Each argument can be configured separately {@link Argument}
     *
     * @return list if required arguments
     */
    List<Argument> getRequiredArguments();

    /**
     * Adding a new argument to option.
     * Required arguments are stored in an option
     * Each option can have zero to many arguments
     *
     * @param argument the required argument
     */
    void addRequiredArguments(Argument argument);

}
