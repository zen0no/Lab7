package dataClasses;

import exceptions.IllegalModelFieldException;
import exceptions.ModelFieldException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Data class
 */
public class Car implements Comparable<Car>, Cloneable{

    private static final List<String> fields = List.of("cool");
    private boolean cool;

    /**
     * @param cool boolean
     */
    public Car(boolean cool){
        this.cool = cool;
    }

    /**
     *  Constructor overload with no params
     */
    public Car() {}

    /**
     * @param args map of car fields
     * @return car parsed from map
     * @throws ModelFieldException
     */
    public static Car parseCar(Map<String, String> args) throws ModelFieldException {
        try {
            for (Map.Entry<String, String> f: args.entrySet()){
                if (!getFields().contains(f.getKey())) throw new IllegalModelFieldException("No such field: Car." + f.getKey());
            }
            if (args.containsKey("cool")) {
                String s = args.get("cool");
                if ("true".equals(s.toLowerCase(Locale.ROOT))) return new Car(true);
                else if ("false".equals(s.toLowerCase(Locale.ROOT))) return new Car(false);
                else throw new IllegalModelFieldException("invalid value for Car.cool: \"" + s + "\"");
            }

            return new Car();
        }
        catch (NullPointerException e){
            return null;
        }
    }

    /**
     * @param s string to parse car object from
     * @return car parsed from string
     * @throws IllegalArgumentException
     */
    public static Car parseCar(String s) throws IllegalArgumentException{
        if (s == null) return new Car();
        if ("true".equals(s.toLowerCase(Locale.ROOT))) return new Car(true);
        else if ("false".equals(s.toLowerCase(Locale.ROOT))) return new Car(false);
        else throw new IllegalArgumentException("incorrect format of string");
    }

    /**
     * @return list of car fields
     */
    public static List<String> getFields() {
        return fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return cool == car.cool;
    }

    /**
     * cool getter
     * @return boolean
     */
    public boolean isCool() {
        return cool;
    }

    /**
     * cool setter
     * @param cool boolean
     */
    public void setCool(Boolean cool){
        this.cool = cool;
    }

    @Override
    public String toString() {
        return String.valueOf(cool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cool);
    }

    @Override
    public int compareTo(Car o) {
        return Boolean.compare(isCool(), o.isCool());
    }

    @Override
    public Car clone(){
        try{
            return (Car) super.clone();
        }
        catch(CloneNotSupportedException e){
            return null;
        }

    }
}
