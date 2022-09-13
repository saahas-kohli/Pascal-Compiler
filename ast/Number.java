package ast;

import environment.*;
import emitter.*;

/**
 * The Number class is used to define any number in
 * PASCAL.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Number extends Expression
{
    private int value;

    /**
     * Constructor for a Number object.
     * @param value the numerical value of this Number
     */
    public Number(int value)
    {
        this.value = value;
    }

    /**
     * Outputs the numerical value of this Number.
     * @param env the environment
     * @return the integer value of this Number
     */
    public int eval(Environment env)
    {
        return value;
    }

    @Override
    public void compile(Emitter e)
    {
        e.emit("li $v0, " + value + "\t# loads " + value + " into v0");
    }
}
