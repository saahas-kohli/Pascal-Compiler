package scanner;

import java.io.*;

/**
 * Scanner is a simple scanner for Compilers and Interpreters - Lab Exercise 1.
 * Its purpose is to read an input stream character-by-character and to create
 * a stream of tokens from that input.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 *
 * Usage (iterating through all tokens of an input stream):
 * while(scanner.hasNext()) { scanner.nextToken(); }
 *
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    /**
     * Scanner constructor for construction of a scanner that
     * uses an InputStream object for input.
     * Usage:
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that
     * scans a given input string. It sets the end-of-file flag on, then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Attempts to obtain next character from input stream, setting eof to
     * true if the end of file or '.' is reached on the input stream.
     */
    private void getNextChar()
    {
        try
        {
            int inp = in.read();
            if (inp == -1)
                eof = true;
            currentChar = (char) inp;
            if(currentChar == '.')
                eof = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Advances the reader by a character if currentChar matches
     * the expected character
     * @param expected the expected character to move past
     * @throws ScanErrorException if expected & current characters do not match
     */
    private void eat(char expected) throws ScanErrorException
    {
        if(expected == currentChar)
            getNextChar();
        else
            throw new ScanErrorException("Illegal character - expected \"" + currentChar +
                    "\" and found \"" + expected + ".\"");
    }

    /**
     * Determines if there is anything left in the input that has not
     * yet been read.
     * @return true if reader is not at end of input; otherwise, false
     */
    public boolean hasNext()
    {
        return ! eof;
    }

    /**
     * Determines if a given character is a numerical digit.
     * @param c the character to be checked
     * @return true if c is a digit; otherwise, false
     */
    public static boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    /**
     * Determines if a given character is an English letter.
     * @param c the character to be checked
     * @return true if c is a letter in the alphabet; otherwise, false
     */
    public static boolean isLetter(char c)
    {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    /**
     * Determines if a given character is whitespace.
     * @param c the character to be checked
     * @return true if c is whitespace; otherwise, false
     */
    public static boolean isWhiteSpace(char c)
    {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    /**
     * Determines if a given character is "special"
     * (an operator or delimiter of some sort).
     * @param c the character to be checked
     * @return true if c is special; otherwise, false
     */
    public static boolean isSpecialCharacter(char c)
    {
        return c == '(' || c == ')' || c == ':' || c == '+' || c == '-' ||
                c == '*' || c == '/' || c == '<' || c == '>' || c == '=' ||
                c == ',' || c == ';' || c == '%' || c == '{' || c == '}' ||
                c == '[' || c == ']';
    }

    /**
     * Scans a complete number token from the input.
     * @return a String representing a number lexeme, the next
     *         token from the input
     * @throws ScanErrorException thrown in the case of a scanning
     *                            error
     */
    private String scanNumber() throws ScanErrorException
    {
        String lexeme = "";
        while(hasNext() && isDigit(currentChar))
        {
            lexeme += currentChar;
            eat(currentChar);
        }
        return lexeme;
    }

    /**
     * Scans an entire identifier token from the input.
     * @return a String representing an identifier lexeme, the next
     *         token from the input
     * @throws ScanErrorException thrown in the case of a scanning
     *                            error
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String lexeme = "";
        while(hasNext() && (isDigit(currentChar) || isLetter(currentChar)))
        {
            lexeme += currentChar;
            eat(currentChar);
        }
        return lexeme;
    }

    /**
     * Scans an entire operator token from the input.
     * @return a String representing an operator lexeme, the next
     *         token from the input
     * @throws ScanErrorException thrown in the case of a scanning
     *                            error
     */
    private String scanOperator() throws ScanErrorException
    {
        String lexeme = "" + currentChar;
        eat(currentChar);
        if(("=>").contains("" + currentChar) && ("+-=/*%><:").contains(lexeme))
        {
            lexeme += currentChar;
            eat(currentChar);
        }
        return lexeme;
    }

    /**
     * Scans for single-line or multi-line comments, advancing the
     * reader through an entire comment (if any).
     * @return a String representing the next token from the input
     * @throws ScanErrorException thrown in the case of a scanning
     *                            error
     */
    private String scanComments() throws ScanErrorException
    {
        eat(currentChar);
        if(currentChar == '/')
        {
            while(hasNext() && currentChar != '\n' && currentChar != '\r')
                eat(currentChar);
            eat(currentChar);
        }
        else if(currentChar == '*')
        {
            eat(currentChar);
            while(hasNext())
            {
                while (hasNext() && currentChar != '*')
                    eat(currentChar);
                eat(currentChar);
                if(currentChar == '/')
                {
                    eat(currentChar);
                    break;
                }
            }
        }
        else if(currentChar == '=')
            return "/=";
        else
            return "/";
        return nextToken();
    }

    /**
     * Retrieves the next token from the input stream,
     * advancing the reader as necessary.
     * @return a String representing the next token from
     *         the input
     * @throws ScanErrorException thrown in the case of a scanning
     *                            error
     */
    public String nextToken() throws ScanErrorException
    {
        while (hasNext() && isWhiteSpace(currentChar))
            eat(currentChar);
        if (! hasNext())
            return ".";
        if (isDigit(currentChar))
            return scanNumber();
        if(isLetter(currentChar))
            return scanIdentifier();
        if(currentChar == '/')
            return scanComments();
        if (isSpecialCharacter(currentChar))
            return scanOperator();
        //char prev = currentChar;
        eat(currentChar);
        return nextToken();
        //throw new ScanErrorException("Encountered an unknown character: \"" + prev + "\".");
    }

    public String next()
    {
        try
        {
            return nextToken();
        }
        catch(ScanErrorException e)
        {
            return next();
        }
    }

}


