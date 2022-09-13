package ast;

import environment.*;
import emitter.*;

/**
 * The Statement class is used to abstractly define
 * PASCAL statements to be executed.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public abstract class Statement
{
    /**
     * Executes the code in this statement.
     * @param env the environment
     */
    public abstract void exec(Environment env);

    /**
     * Compiles the PASCAL Statement to MIPS.
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!");
    }
}
