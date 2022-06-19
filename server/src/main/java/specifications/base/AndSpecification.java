package specifications.base;

/**
 * And specification
 * @param <T>
 */
public class AndSpecification<T> extends CompositeSpecification<T>{
    private final Specification<T> left;
    private final Specification<T> right;

    public AndSpecification(final Specification<T> left, final Specification<T> right){
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedBy(T candidate){
        return left.isSatisfiedBy(candidate) && right.isSatisfiedBy(candidate);
    }
}
