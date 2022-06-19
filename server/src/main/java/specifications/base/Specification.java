package specifications.base;

public interface Specification<T> {
    /**
     * @param candidate entity to verify
     * @return true if entity satisfies all conditions, false otherwise
     */
    public boolean isSatisfiedBy(T candidate);

    /**
     * @param other specification, contains condition to conjunct
     * @return conjunct of specifications
     */
    Specification<T> And(Specification<T> other);

    /**
     * @param other specification, contains condition to disjunct
     * @return disjunct of specifications
     */
    Specification<T> Or(Specification<T> other);

    /**
     * @param other specification, contains condition to negate
     * @return negation of specification
     */
    Specification<T> Not(Specification<T> other);

}
