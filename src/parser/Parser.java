package parser;

import scanner.*;

import java.util.List;
import java.util.ArrayList;
import ast.*;

/**
 * The Parser class's goal is to take the token stream from a lexical analyzer
 * (scanner) and parse every PASCAL statement.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Parser
{

    private Scanner sc;
    private String currToken;

    /**
     * Constructor for a Parser object, which is capable of
     * parsing a PASCAL input file given the token stream
     * from a scanner.
     * @param in the lexical analyzer (scanner) for the input
     *           PASCAL file; used to generate tokens from input
     */
    public Parser(Scanner in)
    {
        sc = in;
        currToken = sc.next();
    }

    /**
     * Eats the current token if it matches a given expected token.
     * @param expToken the expected token to be compared to currToken
     * @throws IllegalArgumentException if expected token and currToken
     *                                  do not match
     */
    private void eat(String expToken) throws IllegalArgumentException
    {
        if(expToken.equals(currToken))
        {
            currToken = sc.next();
        }
        else throw new IllegalArgumentException("Expected \"" + expToken + "\" but found \"" +
                currToken + "\"");
    }

    /**
     * Parses one full PASCAL program (arbitrary number of procedure declarations
     * followed by a statement and eventually a period).
     * @precondition either at the beginning of a Procedure declaration or start of
     *               a Statement
     * @postcondition all tokens in the Program have been eaten
     * @return the Program that was parsed
     */
    public Program parseProgram()
    {
        List<VariableDeclaration> vars = new ArrayList<>();
        if(currToken.equals("("))
            eat("(");
        while(currToken.equals("VAR"))
        {
            eat("VAR");
            String id = currToken;
            eat(currToken);
            vars.add(new VariableDeclaration(id));
            while(! currToken.equals(";"))
            {
                eat(",");
                id = currToken;
                eat(currToken);
                vars.add(new VariableDeclaration(id));
            }
            eat(";");
            if(currToken.equals(")"))
                eat(")");
        }
        List<ProcedureDeclaration> procDecList = new ArrayList<ProcedureDeclaration>();
        while(currToken.equals("PROCEDURE"))
        {
            eat("PROCEDURE");
            String procName = currToken;
            eat(currToken);
            eat("(");
            List<String> params = new ArrayList<>();
            while(! currToken.equals(")"))
            {
                params.add(currToken);
                eat(currToken);
                if(currToken.equals(","))
                    eat(",");
            }
            eat(")");
            eat(";");
            List<String> locVars = new ArrayList<>();
            while(currToken.equals("VAR"))
            {
                eat("VAR");
                locVars.add(currToken);
                eat(currToken);
                while(currToken.equals(","))
                {
                    eat(",");
                    locVars.add(currToken);
                    eat(currToken);
                }
                eat(";");
            }
            Statement procStatement = parseStatement();
            procDecList.add(new ProcedureDeclaration(procName, procStatement, params, locVars));
        }
        Statement statement = parseStatement();
        eat(".");
        return new Program(vars, procDecList, statement);
    }

    /**
     * Parses one full PASCAL statement (block, writeln, if, or while statement).
     * @precondition currToken is WRITELN, BEGIN, IF, or WHILE (or at
     *               end of file)
     * @postcondition all tokens in the Statement have been eaten
     * @return the Statement that was parsed
     */
    public Statement parseStatement()
    {
        if(currToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(exp);
        }
        else if(currToken.equals("BEGIN"))
        {
            eat("BEGIN");
            List<Statement> stmts = new ArrayList<Statement>();
            while(! currToken.equals("END"))
                stmts.add(parseStatement());
            eat("END");
            eat(";");
            return new Block(stmts);
        }
        else if(currToken.equals("IF"))
        {
            eat("IF");
            Expression exp1 = parseExpression();
            String relop = currToken;
            eat(currToken);
            Expression exp2 = parseExpression();
            Condition cond = new Condition(exp1, exp2, relop);
            eat("THEN");
            Statement condStmt = parseStatement();
            return new If(cond, condStmt);
        }
        else if(currToken.equals("WHILE"))
        {
            eat("WHILE");
            Expression exp1 = parseExpression();
            String relop = currToken;
            eat(currToken);
            Expression exp2 = parseExpression();
            Condition cond = new Condition(exp1, exp2, relop);
            eat("DO");
            Statement condStmt = parseStatement();
            return new While(cond, condStmt);
        }
        else
        {
            String variable = currToken;
            eat(currToken);
            eat(":=");
            Expression expr = parseExpression();
            eat(";");
            return new Assignment(variable, expr);
        }
    }

    /**
     * Parses a PASCAL number, which is just any numerical
     * integer.
     * @precondition current token is an integer
     * @postcondition number token has been eaten
     * @return the Number that was parsed
     */
    private ast.Number parseNumber()
    {
        int num = Integer.parseInt(currToken);
        eat("" + num);
        return new ast.Number(num);
    }

    /**
     * Parses a PASCAL factor, which is any expression that
     * we can multiply, divide, or mod.
     * @precondition currToken is a (, -, number, or identifier
     * @postcondition all tokens in the factor have been eaten
     * @return the expression that was parsed (a factor)
     */
    private Expression parseFactor()
    {
        if(currToken.equals("("))
        {
            eat(currToken);
            Expression expr = parseExpression();
            eat(")");
            return expr;
        }
        else if(currToken.equals("-"))
        {
            eat(currToken);
            return new BinOp("*", new ast.Number(-1), parseFactor());
        }
        else
        {
            try
            {
                return parseNumber();
            }
            catch(NumberFormatException e)
            {
                String id = currToken;
                eat(currToken);
                if(currToken.equals("("))
                {
                    eat("(");
                    List<Expression> args = new ArrayList<Expression>();
                    while(! currToken.equals(")"))
                    {
                        args.add(parseExpression());
                        if(currToken.equals(","))
                            eat(",");
                    }
                    eat(")");
                    return new ProcedureCall(id, args);
                }
                return new Variable(id);
            }
        }
    }

    /**
     * Parses a PASCAL term, which is any expression that might
     * be added or subtracted.
     * @precondition currToken is a (, -, number, or identifier
     * @postcondition all tokens in the term have been eaten
     * @return the Expression that was parsed (a term)
     */
    private Expression parseTerm()
    {
        Expression factor = parseFactor();
        while(currToken.equals("*") || currToken.equals("/") || currToken.equals("mod"))
        {
            String temp = currToken;
            eat(currToken);
            if(temp.equals("*"))
                factor = new BinOp("*", factor, parseFactor());
            else if(temp.equals("/"))
                factor = new BinOp("/", factor, parseFactor());
            else
                factor = new BinOp("%", factor, parseFactor());
        }
        return factor;
    }

    /**
     * Parses a PASCAL expression, which consists of any
     * sequence of operators and operands.
     * @precondition currToken is a (, -, number, or identifier
     * @postcondition all tokens in the expression have been eaten
     * @return the Expression that was parsed
     */
    private Expression parseExpression()
    {
        Expression term = parseTerm();
        while(currToken.equals("+") || currToken.equals("-"))
        {
            String temp = currToken;
            eat(currToken);
            if(temp.equals("+"))
                term = new BinOp("+", term, parseTerm());
            else
                term = new BinOp("-", term, parseTerm());
        }
        return term;
    }

}

