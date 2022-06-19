package repositories;

import dataClasses.HumanBeing;

import other.InfoBlock;
import specifications.base.CompositeSpecification;
import specifications.base.Specification;

import java.time.ZonedDateTime;
import java.util.*;


/**
 * Data storage class. Implements repository and extends linkedhashmap
 * Contains instances of HumanBeing
 */
public class HumanBeingRepository  implements Repository<HumanBeing> {

    protected ZonedDateTime initDate;
    protected int primaryKeyCounter = 0;
    protected Map<String, HumanBeing> values = new HashMap<>();


    /**
     * Constructor. Loads data from file, if it exists
     */
    public HumanBeingRepository(){

    }


    public InfoBlock getInfo(){
        return new InfoBlock(initDate, values.size(), "HumanBeing");
    }


    @Override
    public List<HumanBeing> insertEntity(HumanBeing entity) {
        entity.setId(primaryKeyCounter++);
        values.put(entity.getPrimaryKey(), entity);
        return List.of(entity);
    }

    @Override
    public List<HumanBeing> insertEntity(List<HumanBeing> entities) {
        ArrayList<HumanBeing> res = new ArrayList<>();
        for (HumanBeing h: entities){
            res.addAll(insertEntity(h));
        }
        return res;
    }

    private List<HumanBeing> removeEntity(HumanBeing entity) {
        values.remove(entity.getPrimaryKey());
        return List.of(entity);
    }


    @Override
    public List<HumanBeing> removeEntity(List<HumanBeing> humanBeings){
        for (HumanBeing h: humanBeings){
            removeEntity(h);
        }
        return humanBeings;
    }

    @Override
    public List<HumanBeing> updateEntity(HumanBeing entity){
        values.replace(entity.getPrimaryKey(), entity);
        return List.of(entity);
    }

    @Override
    public List<HumanBeing> updateEntity(List<HumanBeing> entities){
        for (HumanBeing h: entities){
            updateEntity(h);
        }

        return entities;
    }

    @Override
    public List<HumanBeing> query(Specification<HumanBeing> specification) {
        return new ArrayList<>(values.values().stream().filter(specification::isSatisfiedBy).toList());
    }

    @Override
    public List<HumanBeing> query() {
        return query(new CompositeSpecification<>() {
            @Override
            public boolean isSatisfiedBy(HumanBeing candidate) {
                return true;
            }
        });
    }
}
