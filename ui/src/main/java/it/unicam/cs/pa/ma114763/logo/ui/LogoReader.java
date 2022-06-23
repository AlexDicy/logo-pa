package it.unicam.cs.pa.ma114763.logo.ui;

import it.unicam.cs.pa.ma114763.logo.drawing.Color;
import it.unicam.cs.pa.ma114763.logo.drawing.Point;
import it.unicam.cs.pa.ma114763.logo.drawing.Position2D;
import it.unicam.cs.pa.ma114763.logo.drawing.RGBColor;
import it.unicam.cs.pa.ma114763.logo.shape.Line;
import it.unicam.cs.pa.ma114763.logo.shape.Polygon;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lorenzo Lapucci
 */
public class LogoReader {

    /**
     * @param serialized the command string
     * @return the corresponding line
     * @throws IllegalArgumentException if the command string is not valid for a line
     * @see Line#serialize() for the format of the serialized command
     */
    public Line deserializeLine(String serialized) {
        String[] tokens = serialized.split(" +");
        if (tokens.length < 8 || tokens.length > 9) {
            throw new IllegalArgumentException("Invalid serialized command for LINE: " + serialized);
        }
        try {
            Position2D start = new Point(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]));
            Position2D end = new Point(Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4]));
            int alpha = tokens.length == 9 ? Integer.parseInt(tokens[8]) : 255;
            Color color = new RGBColor(Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), Integer.parseInt(tokens[7]), alpha);
            int size = Integer.parseInt(tokens[tokens.length - 1]);
            return new Line(start, end, size, color);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric values for serialized command: " + serialized, e);
        }
    }

    /**
     * Converts the command string into a {@link Polygon} if possible
     *
     * @param serialized the command string
     * @return the {@link Polygon} object that corresponds to the command string
     * @throws IllegalArgumentException if the command string is not valid for a polygon
     * @see Polygon#serialize() for the format of the serialized command
     */
    public Polygon deserializePolygon(String serialized) {
        String[] lines = serialized.split("\n");
        if (lines.length < 4) {
            throw new IllegalArgumentException("A polygon must have at least 3 strokes");
        }
        try {
            String[] tokens = lines[0].split(" +");
            if (tokens.length < 5 || tokens.length > 6) {
                throw new IllegalArgumentException("Invalid serialized command for POLYGON: " + lines[0]);
            }
            int n = Integer.parseInt(lines[1]);
            if (n != lines.length - 1) {
                throw new IllegalArgumentException("Invalid number of strokes in POLYGON: " + serialized);
            }
            int alpha = tokens.length == 6 ? Integer.parseInt(tokens[5]) : 255;
            Color color = new RGBColor(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), alpha);
            List<Line> strokes = getPolygonStrokes(lines);
            return new Polygon(color, strokes);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric values for serialized command: " + serialized, e);
        }
    }

    private static List<Line> getPolygonStrokes(String[] lines) {
        List<Line> strokes = new ArrayList<>(lines.length - 1);
        for (int i = 1; i < lines.length; i++) {
            Line line = getPolygonStrokeLine(lines[i], lines[(i + 1) % lines.length]);
            if (line == null) {
                throw new IllegalArgumentException("Invalid serialized command for POLYGON strokes: " + lines[i] + "; " + lines[(i + 1) % lines.length]);
            }
            strokes.add(line);
        }
        return strokes;
    }

    private static @Nullable Line getPolygonStrokeLine(String line, String nextLine) {
        String[] tokens = line.split(" +");
        String[] tokensNext = nextLine.split(" +");
        if (tokens.length < 6 || tokensNext.length < 6) {
            return null;
        }
        try {
            Position2D start = new Point(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
            Position2D end = new Point(Double.parseDouble(tokensNext[0]), Double.parseDouble(tokensNext[1]));
            int alpha = tokens.length == 7 ? Integer.parseInt(tokens[5]) : 255;
            Color color = new RGBColor(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), alpha);
            int size = Integer.parseInt(tokens[tokens.length - 1]);
            return new Line(start, end, size, color);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
