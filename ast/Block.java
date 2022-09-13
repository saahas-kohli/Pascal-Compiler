package ast;

import java.util.List;
import environment.*;
import emitter.*;

/**
 * The Block class is used to define BEGIN-END block
 * statements in PASCAL.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * Constructor for a Block object.
     * @param stmts an ordered list of every statement in the block
     */
    public Block(List<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Executes the block statement entirely by executing
     * every statement in its list of statements.
     * @param env the environment
     */
    public void exec(Environment env)
    {
        for(Statement stmt: stmts)
            stmt.exec(env);
    }

    @Override
    public void compile(Emitter e)
    {
        for(Statement stmt: stmts)
            stmt.compile(e);
    }
}
