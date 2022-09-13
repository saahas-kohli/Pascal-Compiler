package ast;

import environment.*;
import emitter.*;

/**
 * The BinOp class is used to define binary arithmetic operations.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class BinOp extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Constructor for a BinOp object.
     * @param op the operator being used on the 2 expressions
     * @param exp1 first expression
     * @param exp2 second expression
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the binary operation.
     * @param env the environment
     * @return the integer result of the operation
     */
    public int eval(Environment env)
    {
        int int1 = exp1.eval(env);
        int int2 = exp2.eval(env);
        if(op.equals("+"))
            return int1 + int2;
        else if(op.equals("-"))
            return int1 - int2;
        else if(op.equals("*"))
            return int1 * int2;
        else if(op.equals("/"))
            return int1 / int2;
        else
            return int1 % int2;
    }

    @Override
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if(op.equals("+"))
            e.emit("addu $v0, $t0, $v0\t# stores t0 + v0 in v0");
        else if(op.equals("-"))
            e.emit("subu $v0, $t0, $v0\t# stores t0 - v0 in v0");
        else if(op.equals("*"))
        {
            e.emit("mult $t0, $v0\t# multiplies t0 and v0");
            e.emit("mflo $v0\t#stores product in v0");
        }
        else if(op.equals("/"))
        {
            e.emit("div $t0, $v0\t# divides t0 by v0");
            e.emit("mflo $v0\t# stores quotient in v0");
        }
        else
        {
            e.emit("div $t0, $v0\t# divides t0 by v0");
            e.emit("mfhi $v0\t# stores remainder in v0");
        }
    }
}
