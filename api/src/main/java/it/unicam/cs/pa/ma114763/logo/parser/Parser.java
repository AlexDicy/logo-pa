package it.unicam.cs.pa.ma114763.logo.parser;

import it.unicam.cs.pa.ma114763.logo.Processor;
import it.unicam.cs.pa.ma114763.logo.Statement;
import it.unicam.cs.pa.ma114763.logo.parser.exception.ParserException;

import java.util.List;

/**
 * This class is used to parse the input string and create a list of statements.
 * Parsed statements are then executed on a canvas by the {@link Processor}.
 *
 * @author Lorenzo Lapucci
 */
public interface Parser {
    /**
     * Takes the input string and tries to parse it into a list of statements.
     * If any error occurs, a {@link ParserException} is thrown.
     *
     * @param input the input string
     * @return the list of statements
     * @throws ParserException if any error occurs
     */
    List<Statement> parse(String input) throws ParserException;
}
