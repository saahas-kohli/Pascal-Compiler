package ast;

import environment.*;
import emitter.*;

/**
 * The If class is used to define a conditional IF-THEN statement.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class If extends Statement
{
    private Condition cond;
    private Statement condStmt;

    /**
     * Constructor for If objects.
     * @param cond the condition (boolean expression) to be checked
     * @param condStmt the conditional statement
     */
    public If(Condition cond, Statement condStmt)
    {
        this.cond = cond;
        this.condStmt = condStmt;
    }

    /**
     * Executes the IF-THEN statement.
     * @param env the environment
     */
    public void exec(Environment env)
    {
        if(cond.eval(env) == 1)
            condStmt.exec(env);
    }

    @Override
    public void compile(Emitter e)
    {
        String branchLabel = "endIf" + e.nextLabelID();
        cond.compile(e, branchLabel);
        condStmt.compile(e);
        e.emit(branchLabel + ":");
    }
}
