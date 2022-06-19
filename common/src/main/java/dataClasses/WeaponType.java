package dataClasses;

import exceptions.IllegalModelFieldException;

import java.util.EnumSet;

public enum WeaponType implements Comparable<WeaponType>{
    AXE("axe"),
    PISTOL("pistol"),
    KNIFE("knife"),
    BAT("bat"),
    ;

    private final String description;

    WeaponType(String description){
        this.description = description;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param s string to parse weaponType from
     * @return weaponType
     * @throws IllegalModelFieldException
     */
    public static WeaponType parseWeaponType(String s) throws IllegalModelFieldException {
        if (s == null) return null;
        for(WeaponType w: EnumSet.allOf(WeaponType.class)){
            if (w.getDescription().equals(s)) return w;
        }

        throw new IllegalModelFieldException("can't parse weaponType from \"" + s + "\"");
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
