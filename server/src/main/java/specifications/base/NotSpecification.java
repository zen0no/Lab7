package specifications.base;

/**
 * Negative specification
 * @param <T>
 */
public class NotSpecification<T> extends CompositeSpecification<T>{
    private final Specification<T> specification;

    public NotSpecification(final Specification<T> specification){
        this.specification = specification;
    }

    public boolean isSatisfiedBy(T candidate){
        return !specification.isSatisfiedBy(candidate);
    }
}
