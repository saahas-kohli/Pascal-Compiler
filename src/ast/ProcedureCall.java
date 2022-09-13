package ast;

import environment.*;
import java.util.List;
import emitter.Emitter;

/**
 * The ProcedureCall class is used to define a Procedure call
 * in PASCAL (with the appropriate number of arguments).
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class ProcedureCall extends Expression
{
    private String name;
    private List<Expression> args;

    /**
     * Constructor for a ProcedureCall object.
     * @param name the name of the Procedure to be called
     * @param args the arguments in the ProcedureCall
     */
    public ProcedureCall(String name, List<Expression> args)
    {
        this.name = name;
        this.args = args;
    }

    /**
     * Evaluates the return value of this ProcedureCall.
     * @param env the environment
     * @return integer output of the Procedure call
     */
    public int eval(Environment env)
    {
        ProcedureDeclaration procDec = env.getProcedure(name);
        Environment child = new Environment(env);
        for(int i = 0; i < args.size(); i++)
        {
            int value = args.get(i).eval(env);
            child.declareVariable(procDec.getParams().get(i), value);
        }
        child.declareVariable(name, 0);
        procDec.getStatement().exec(child);
        return child.getVariable(name);
    }

    @Override
    public void compile(Emitter e)
    {
        e.emitPush("$ra");
        for(Expression arg: args)
        {
            arg.compile(e);
            e.emitPush("$v0");
        }
        //e.emit("li $v0, 0");
        e.emitPush("$v0");
        e.emit("jal proc" + name);
        e.emitPop("$v0");
        for (int i = 0; i < args.size(); i++)
            e.emitPop("$v0");
        e.emitPop("$ra");
    }
}
