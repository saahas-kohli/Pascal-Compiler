package ast;

import environment.*;
import emitter.*;

/**
 * The While class is used to define a WHILE-DO statement.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class While extends Statement
{
    private Condition cond;
    private Statement condStmt;

    /**
     * Constructor for a While object.
     * @param cond the condition (boolean expression) to be checked
     * @param condStmt the conditional statement (body of loop)
     */
    public While(Condition cond, Statement condStmt)
    {
        this.cond = cond;
        this.condStmt = condStmt;
    }

    /**
     * Executes the WHILE-DO statement.
     * @param env the environment
     */
    public void exec(Environment env)
    {
        while(cond.eval(env) == 1)
            condStmt.exec(env);
    }

    @Override
    public void compile(Emitter e)
    {
        String loopLabel = "while" + e.nextLabelID();
        String branchLabel = "endwhile" + e.nextLabelID();
        e.emit(loopLabel + ":");
        cond.compile(e, branchLabel);
        condStmt.compile(e);
        e.emit("j " + loopLabel + "\t# starts another iteration of the loop");
        e.emit(branchLabel + ":");
    }
}

