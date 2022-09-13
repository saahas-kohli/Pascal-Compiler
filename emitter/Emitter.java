package emitter;

import java.io.*;
import ast.ProcedureDeclaration;
import java.util.List;

/**
 * The Emitter class enables creating an output file
 * and writing to it with MIPS Assembly code.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Emitter
{
    private PrintWriter out;
    private int nextLabelID;
    private ProcedureDeclaration currProcDec;
    private int excessStackHeight;

	/**
	 * Creates an emitter that writes to a new file with the given name.
	 * @param outputFileName String denoting name of output file
	 */
    public Emitter(String outputFileName)
    {
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        nextLabelID = 0;
    }

	/**
	 * Provides an id number for the next MIPS label
	 * (to ensure that all labels are different).
	 * @return integer denoting the next label ID
	 */
    public int nextLabelID()
    {
        nextLabelID++;
        return nextLabelID;
    }

	/**
	 * Prints one line of code to file (with non-labels indented).
	 * @param code line of code to be written to file
	 */
    public void emit(String code)
    {
        if (!code.endsWith(":") && !code.startsWith("#"))
            code = "\t" + code;
        out.println(code);
    }

	/**
	 * Pushes the value of a given MIPS register onto
	 * the stack.
	 * @param reg String denoting the name of the MIPS
	 * 	          register that is pushed onto the stack
	 */
    public void emitPush(String reg)
    {
        this.emit("subu $sp, $sp, 4\t# allocating 4 bytes of memory");
        this.emit("sw " + reg + ", ($sp)\t# pushes " + reg + " onto the stack");
    }

	/**
	 * Pops the top element of the stack onto a specified
	 * MIPS register.
	 * @param reg String denoting the name of the MIPS
	 *            register where the popped value goes
	 */
    public void emitPop(String reg)
    {
        this.emit("lw " + reg + ", ($sp)\t# pops top element on stack onto " + reg);
        this.emit("addu $sp, $sp, 4\t# removing the previously occupied 4 bytes of memory");
    }

    /**
     * Sets the procedure context to the given ProcedureDeclaration.
     * @param procDec new procedure context to be set
     */
    public void setProcedureContext(ProcedureDeclaration procDec)
    {
        currProcDec = procDec;
        excessStackHeight = 0;
    }

    /**
     * Clears the procedure context (to null).
     */
    public void clearProcedureContext()
    {
        currProcDec = null;
    }

    /**
     * Determines if a given variable is local or global.
     * @param varName the name of the variable whose locality is being checked
     * @return true if the given variable is local to the ProcedureDeclaration;
     *         otherwise, false
     */
    public boolean isLocalVariable(String varName)
    {
        if(currProcDec == null)
            return false;
        if(varName.equals(currProcDec.getName()))
            return true;
        List<String> paramList = currProcDec.getParams();
        for(String param: paramList)
            if(param.equals(varName))
                return true;
        List<String> locVars = currProcDec.getLocVars();
        for(String locVar: locVars)
            if(varName.equals(locVar))
                return true;
        return false;
    }

    /**
     * Determines the positional offset of a certain variable from the top of the stack.
     * @param localVarName name of variable whose offset is being found
     * @return offset of varName in the stack if varName is defined; otherwise, -1
     */
    public int getOffset(String localVarName)
    {
        int offset = excessStackHeight;
        for(int i = 0; i < currProcDec.getLocVars().size(); i++)
        {
            if(localVarName.equals(currProcDec.getLocVars().get(i)))
                return offset;
            offset += 4;
        }
        if (localVarName.equals(currProcDec.getName()))
            return offset;
        offset += 4;
        for (int i = currProcDec.getParams().size() -1; i >= 0; i--)
        {
            if(localVarName.equals(currProcDec.getParams().get(i)))
                return offset;
            offset += 4;
        }
        return -1;
    }

	/**
	 * Closes the file. Should be called after all calls to emit.
	 */
    public void close()
    {
        out.close();
    }
}