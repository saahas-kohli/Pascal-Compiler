package ast;

import scanner.*;
import parser.Parser;
import environment.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * The purpose of this Tester class is to test compilation
 * of PASCAL to MIPS.
 *
 * @author Saahas Kohli
 * @version 11/19/2021
 */
public class Tester
{
    /**
     * Compiles a PASCAL program to MIPS code,
     * which is then written to an output file.
     * @param args functions from command line
     * @throws FileNotFoundException if file path does not exist
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        for(int i = 1; i < 5; i++)
        {
            System.out.println("File " + i + ": ");
            FileInputStream inStream;
            inStream = new FileInputStream(("parserTests/parserTest" + i + ".txt"));
            Scanner scanner = new Scanner(inStream);
            Parser parser = new Parser(scanner);
            // Environment env = new Environment();
            Program program = parser.parseProgram();
            program.compile("parserTest" + i + "_output.txt");
            System.out.println("Compiled!");
        }
        System.out.println("Done! Check MIPS output file.");
    }
}

