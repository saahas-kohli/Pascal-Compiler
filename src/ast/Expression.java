package ast;

import environment.*;
import emitter.*;

/**
 * The Expression class is used to abstractly define
 * PASCAL expressions that can be evaluated.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public abstract class Expression
{
    /**
     * Evaluates the value of this expression.
     * @param env the environment
     * @return integer value of the expression
     */
    public abstract int eval(Environment env);

    /**
     * Compiles the PASCAL Expression to MIPS.
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!");
    }
}
