package dataClasses;

import exceptions.IllegalModelFieldException;

import java.util.EnumSet;

public enum Mood implements Comparable<Mood>{
    SADNESS("sadness"),
    SORROW("sorrow"),
    GLOOM("gloom"),
    APATHY("apathy"),
    FRENZY("frenzy"),
    ;

    private final String description;

    Mood(String s){
        this.description = s;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param s string to parse mood from
     * @return mood
     * @throws IllegalModelFieldException
     */
    public static Mood parseMood(String s) throws IllegalModelFieldException {
        if (s == null){
            return null;
        }
        for (Mood m:
                EnumSet.allOf(Mood.class)) {
            if (m.getDescription().equals(s)) return m;
        }
        throw new IllegalModelFieldException("can't parse mood from \"" + s + "\"");
    }


    @Override
    public String toString() {
        return getDescription();
    }
}
