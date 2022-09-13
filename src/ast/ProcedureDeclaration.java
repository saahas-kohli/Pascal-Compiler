package ast;
import emitter.Emitter;

import environment.*;
import java.util.List;

/**
 * The ProcedureDeclaration class is used to define a
 * Procedure declaration in PASCAL (with any number of
 * parameters).
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class ProcedureDeclaration extends Statement
{
    private String name;
    private Statement statement;
    private List<String> params;
    private List<String> locVars;

    /**
     * Constructor for a ProcedureDeclaration object.
     * @param name String denoting the name of the Procedure
     * @param statement Statement comprising the body of this Procedure
     * @param params a list of all this Procedure's parameters
     * @param locVars a list of all this Procedure's local variables
     */
    public ProcedureDeclaration(String name, Statement statement, List<String> params, List<String> locVars)
    {
        this.name = name;
        this.statement = statement;
        this.params = params;
        this.locVars = locVars;
    }

    /**
     * Defines this Procedure in the appropriate Environment.
     * @param env the environment
     */
    public void exec(Environment env)
    {
        env.setProcedure(name, this);
    }

    /**
     * Provides the body of this Procedure.
     * @return a Statement denoting the Procedure body
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Provides a list of all parameters.
     * @return a list of Strings denoting all the parameter names
     */
    public List<String> getParams()
    {
        return params;
    }

    /**
     * Provides a list of all local variables.
     * @return a list of Strings denoting all the local variable names
     */
    public List<String> getLocVars() { return locVars; }

    /**
     * Gives the name of this Procedure
     * @return String denoting the Procedure's name
     */
    public String getName()
    {
        return name;
    }

    @Override
    public void compile(Emitter e)
    {
        //e.emit("li $v0, 0");
        //e.emitPush("$v0");
        e.setProcedureContext(this);
        e.emit("proc" + name + ":");
        //for(String param: params)
        //{
            //e.emit("li $v0, 0");
            //e.emitPush("$v0");
        //}
        statement.compile(e);
        //for (String param: params)
            //e.emitPop("$v0");
        //e.emitPop("$v0");
        e.emit("jr $ra");
        e.clearProcedureContext();
    }
}
