package ast;

import environment.*;
import emitter.*;

/**
 * The Variable class is used to define a variable in PASCAL.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Variable extends Expression
{
    private String name;

    /**
     * Constructor for a Variable object.
     * @param name the String identifier of the variable
     */
    public Variable(String name)
    {
        this.name = name;
    }

    /**
     * Outputs the value referenced by this variable.
     * @param env the environment
     * @return the integer value stored by the variable
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }

    @Override
    public void compile(Emitter e)
    {
        if(! e.isLocalVariable(name))
        {
            e.emit("la $t0, var" + name + "\t# loads address of var" + name + " to t0");
            e.emit("lw $v0, ($t0)\t# loads the word referenced by t0 into v0");
        }
        else
            e.emit("lw $v0 " + e.getOffset(name) + "($sp)");
    }
}
