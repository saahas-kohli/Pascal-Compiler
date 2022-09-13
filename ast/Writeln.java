package ast;

import environment.*;
import emitter.*;

/**
 * The Writeln class is used to define a WRITELN statement
 * in PASCAL.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * Constructor for a Writeln object.
     * @param exp the expression whose value will be printed
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Executes the Writeln statement, printing the value of
     * its expression.
     * @param env the environment
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }

    @Override
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0, $v0\t# sets a0 to v0");
        e.emit("li $v0, 1");
        e.emit("syscall\t# prints the integer in a0");
        e.emit("li $v0, 11");
        e.emit("li $a0, 10");
        e.emit("syscall\t# prints character with ASCII value given by a0 (new line)");
    }
}
