package environment;

import java.util.Map;
import java.util.HashMap;
import ast.*;

/**
 * The Environment class keeps track of all variables and procedures.
 * Environments can be the children of other Environments, and the topmost
 * Environment is the global Environment.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Environment
{
    private Map<String, Integer> variables;
    private Map<String, ProcedureDeclaration> procedures;
    private Environment parent;

    /**
     * Constructor for the global Environment.
     */
    public Environment()
    {
        variables = new HashMap<String, Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        parent = null;
    }

    /**
     * Constructor for a child Environment.
     * @param env the parent Environment
     */
    public Environment(Environment env)
    {
        variables = new HashMap<String, Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        parent = env;
    }

    /**
     * Defines a variable in the current Environment.
     * @param variable String denoting the variable's name
     * @param value integer value to be stored by the variable
     */
    public void declareVariable(String variable, int value)
    {
        variables.put(variable, value);
    }

    /**
     * Helper for the setVariable method.
     * @param variable String denoting the variable's name
     * @param value integer value to be stored by the variable
     * @return true if the variable was set; otherwise, false
     */
    public boolean setVariableHelper(String variable, int value)
    {
        if(variables.containsKey(variable))
        {
            variables.put(variable, value);
            return true;
        }
        else if(parent == null)
        {
            return false;
        }
        else
            return parent.setVariableHelper(variable, value);
    }

    /**
     * Defines a variable in the appropriate Environment.
     * @param variable the name of a variable
     * @param value the integer value to be associated
     */
    public void setVariable(String variable, int value)
    {
        if(! setVariableHelper(variable, value))
            variables.put(variable, value);
    }

    /**
     * Outputs integer value of the variable associated with the given name.
     * @param variable the name of the variable to be accessed
     * @return the integer value of the variable
     */
    public int getVariable(String variable)
    {
        if(variables.containsKey(variable))
            return variables.get(variable);
        return parent.getVariable(variable);
    }

    /**
     * Outputs the ProcedureDeclaration mapped by a given name.
     * @param procName String denoting the name of a ProcedureDeclaration
     * @return the ProcedureDeclaration associated with the given Procedure name
     */
    public ProcedureDeclaration getProcedure(String procName)
    {
        if(parent == null)
            return procedures.get(procName);
        return parent.getProcedure(procName);
    }

    /**
     * Associates a Procedure name with a ProcedureDeclaration.
     * @param procName String denoting the name of the given ProcedureDeclaration
     * @param procDec the ProcedureDeclaration to be associated with procName
     */
    public void setProcedure(String procName, ProcedureDeclaration procDec)
    {
        if(parent == null)
            procedures.put(procName, procDec);
        else
            parent.setProcedure(procName, procDec);
    }
}
