package specifications;

import dataClasses.*;
import login.User;
import other.Predicates;
import specifications.base.CompositeSpecification;
import specifications.base.NotSpecification;
import specifications.base.Specification;

import java.util.Date;
import java.util.Objects;

/**
 * Class, contains specification for work with HumanBeing
 */
public class HumanBeingSpecifications {

    /**
     * @param id
     * @return Specification to check id
     */
    public static Specification<HumanBeing> Id(int id){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getId() == id;
            }
        };
    }

    public static Specification<HumanBeing> user(User user){
        return new CompositeSpecification<HumanBeing>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return Objects.equals(user.getLogin(), candidate.getUserLogin());
            }
        };
    }


    /**
     * @param name
     * @return Specification to check name
     */
    public static Specification<HumanBeing> Name(String name){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getName().equals(name);
            }
        };
    }

    /**
     * @param cords
     * @return Specification to check coordinates
     */
    public static Specification<HumanBeing> Coordinates(Coordinates cords){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getCoordinates().equals(cords);
            }
        };
    }

    /**
     * @param date
     * @return to check creation date
     */
    public static Specification<HumanBeing> CreationDate(Date date){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getCreationDate().equals(date);
            }
        };
    }

    /**
     * @param start
     * @param end
     * @return Specification to check if creation date was in certain time interval
     */
    public static Specification<HumanBeing> CreatedInPeriod(Date start, Date end){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getCreationDate().before(end) && candidate.getCreationDate().after(start);
            }
        };
    }

    /**
     * @param has
     * @return Specification to check hasToothpick
     */
    public static Specification<HumanBeing> HasToothpick(boolean has){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.isHasToothpick() == has;
            }
        };
    }

    /**
     * @param realHero
     * @return Specification to check real hero
     */
    public static Specification<HumanBeing> RealHero(boolean realHero){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.isRealHero();
            }
        };
    }

    /**
     * @param speed
     * @return Specification to check if impact speed under value
     */
    public static Specification<HumanBeing> ImpactSpeedUnder(int speed){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return (candidate.getImpactSpeed() < speed);
            }
        };
    }

    /**
     * @param speed
     * @return Specification to check impact speed
     */
    public static Specification<HumanBeing> ImpactSpeed(int speed){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return (candidate.getImpactSpeed() == speed);
            }
        };
    }

    /**
     * @param weaponType
     * @return Specification to check weaponType
     */
    public static Specification<HumanBeing> WeaponType(WeaponType weaponType){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getWeaponType().equals(weaponType);
            }
        };
    }

    /**
     * @param mood
     * @return Specification to check mood
     */
    public static Specification<HumanBeing> Mood(Mood mood){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getMood().equals(mood);
            }
        };
    }

    /**
     * @param car
     * @return specification to check car
     */
    public static Specification<HumanBeing> Car(Car car){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getCar().equals(car);
            }
        };
    }

    /**
     * @param o
     * @return Specification to check humanbeing with lower value
     */
    public static Specification<HumanBeing> Lower(HumanBeing o){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return o.compareTo(candidate) < 0;
            }
        };
    }

    /**
     * @param key
     * @return Specification to check primary key
     */
    public static Specification<HumanBeing> PrimaryKey(String key){
        return new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return candidate.getPrimaryKey().equals(key);
            }
        };
    }


    public static Specification<HumanBeing> fromPredicate(Predicates.Predicate p){
        if (p == null) return null;
        if (p instanceof Predicates.Greater){if (((Predicates.Greater) p).field.equals("HumanBeing")){
            return new NotSpecification<HumanBeing>(HumanBeingSpecifications.Lower((HumanBeing) ((Predicates.Greater) p).value));
        }


    }
        if (p instanceof Predicates.Lower){if (((Predicates.Lower) p).field.equals("HumanBeing")){
            return HumanBeingSpecifications.Lower((HumanBeing) ((Predicates.Lower) p).value);
        }
        }
        if (p instanceof Predicates.Equal){
            if (((Predicates.Equal) p).field.equals("PrimaryKey")){
                return HumanBeingSpecifications.PrimaryKey((String) ((Predicates.Equal) p).value);
            }
        };
        return null;
}
}