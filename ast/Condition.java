package ast;

import environment.*;
import emitter.*;

/**
 * The Condition class is used to define a boolean expression
 * in PASCAL.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Condition extends Expression
{
    private Expression exp1;
    private Expression exp2;
    private String relop;

    /**
     * Constructor for a Condition object.
     * @param exp1 the first expression
     * @param exp2 the second expression
     * @param relop the comparison operator
     */
    public Condition(Expression exp1, Expression exp2, String relop)
    {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.relop = relop;
    }

    /**
     * Determines if this Condition is true.
     * @param env the environment
     * @return 0 if false, 1 if true
     */
    public int eval(Environment env)
    {
        int value1 = exp1.eval(env);
        int value2 = exp1.eval(env);
        if(relop.equals("="))
            return value1 == value2 ? 1 : 0;
        else if(relop.equals("<>"))
            return exp1.eval(env) != exp2.eval(env) ? 1 : 0;
        else if(relop.equals("<"))
            return exp1.eval(env) < exp2.eval(env) ? 1 : 0;
        else if(relop.equals(">"))
            return exp1.eval(env) > exp2.eval(env) ? 1 : 0;
        else if(relop.equals("<="))
            return exp1.eval(env) <= exp2.eval(env) ? 1 : 0;
        else
            return exp1.eval(env) >= exp2.eval(env) ? 1 : 0;
    }

    /**
     * Compiles the PASCAL condition (boolean expression) to MIPS.
     * @param e the emitter
     * @param targetLabel MIPS label to jump to if condition is false
     */
    public void compile(Emitter e, String targetLabel)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        String branchStatement = "";
        String operatorName = "";
        if(relop.equals("="))
        {
            branchStatement = "bne";
            operatorName = "not equal to";
        }
        else if(relop.equals("<>"))
        {
            branchStatement = "beq";
            operatorName = "equal to";
        }
        else if(relop.equals("<"))
        {
            branchStatement = "bge";
            operatorName = "greater than or equal to";
        }
        else if(relop.equals(">"))
        {
            branchStatement = "ble";
            operatorName = "less than or equal to";
        }
        else if(relop.equals("<="))
        {
            branchStatement = "bgt";
            operatorName = "greater than";
        }
        else
        {
            branchStatement = "blt";
            operatorName = "less than";
        }
        e.emit(branchStatement + " $t0, $v0, " + targetLabel + "\t# jumps to "
                + targetLabel + " if t0 is " + operatorName + " v0");
    }
}
