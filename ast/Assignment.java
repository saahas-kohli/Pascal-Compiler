package ast;

import environment.*;
import emitter.*;

/**
 * The Assignment class is used to define variable assignment statements.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Assignment extends Statement
{
    private String var;
    private Expression exp;

    /**
     * Constructs an Assignment object.
     * @param var variable name
     * @param exp expression giving the initial value of the variable
     */
    public Assignment(String var, Expression exp)
    {
        this.var = var;
        this.exp = exp;
    }

    /**
     * Executes the assignment of expression's value
     * to the variable.
     * @param env the environment
     */
    public void exec(Environment env)
    {
        env.setVariable(var, exp.eval(env));
    }

    @Override
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("sw $v0, var" + var + "\t# stores v0 in var" + var);
    }
}
