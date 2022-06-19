package dataClasses;

import exceptions.IllegalModelFieldException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Data class
 */
public class Coordinates implements Comparable<Coordinates>, Cloneable{

    private static final List<String> fields = List.of("x", "y");

    private final long x; // max: 673
    private final long y;

    /**
     * @param x long x-axis coordinate. Max value - 673
     * @param y long y-axis coordinate.
     * @throws IllegalModelFieldException
     */
    public Coordinates(long x, long y) throws IllegalModelFieldException
    {
        if (Math.abs(x) > 673) throw new IllegalModelFieldException("Abs of x is more than maximum possible value");

        this.x = x;
        this.y = y;
    }

    public Coordinates(double x, double y) throws IllegalArgumentException{

        if (Math.abs(x) > 673) throw new IllegalModelFieldException("Abs of x is more than maximum possible value");


        this.x = ((Double) x).longValue();
        this.y = ((Double) y).longValue();
    }

    /**
     * @param args map of coordinates fields
     * @return coordinates
     */
    public static Coordinates parseCoordinates(Map<String, String> args) {
        try {

            if (!fields.containsAll(args.keySet()) && args.keySet().containsAll(fields)) throw new IllegalModelFieldException("Can't parse coordinates");
            long x = Long.parseLong(args.get("x"));
            long y = Long.parseLong(args.get("y"));
            return new Coordinates(x, y);
        } catch (NullPointerException e) {
            return null;
        }
        catch (NumberFormatException e){
            throw new IllegalModelFieldException("can't parse long from this value");
        }
    }

    /**
     * @param s string to parse coordinates from
     * @return coordinates
     */
    public static Coordinates parseCoordinates(String s){
        String[] args = s.split(":");
        try {
            return new Coordinates(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }

        catch (IndexOutOfBoundsException e){
            throw new IllegalModelFieldException("incorrect format of string");
        }
    }


    /**
     * x getter
     * @return x
     */
    public long getX() {
        return x;
    }

    /**
     * y getter
     * @return y
     */
    public long getY() {
        return y;
    }

    /**
     * @return list of coordinates fields
     */
    public static List<String> getFields() {
        return fields;
    }

    @Override
    public int compareTo(Coordinates o){
        if (getX() > o.getX()) return 1;
        if (getX() < o.getX()) return -1;
        if (getY() > o.getY()) return 1;
        if (getY() < o.getY()) return -1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public Coordinates clone(){
        try{
            return (Coordinates) super.clone();
        }
        catch(CloneNotSupportedException e){
            return null;
        }

    }
}
