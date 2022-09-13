package ast;

import environment.*;
import emitter.*;

/**
 * The VariableDeclaration class is used to declare global variables
 * in a MIPS Program.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class VariableDeclaration extends Statement
{
    private String var;

    /**
     * Constructs a VariableDeclaration object.
     * @param var String denoting the variable name
     */
    public VariableDeclaration(String var)
    {
        this.var = var;
    }

    /**
     * Executes the declaration of the variable, giving it
     * a default value of 0.
     * @param env the environment
     */
    public void exec(Environment env)
    {
        env.setVariable(var, 0);
    }

    @Override
    public void compile(Emitter e)
    {
        e.emit("\tvar" + var + ":  .word  " + 0);
    }
}

