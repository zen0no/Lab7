package specifications.base;

/**
 * Or specification
 * @param <T>
 */
public class OrSpecification<T> extends CompositeSpecification<T> {
    private final Specification<T> left;
    private final Specification<T> right;

    public OrSpecification(final Specification<T> left, final Specification<T> right){
        this.left = left;
        this.right = right;
    }

    public boolean isSatisfiedBy(T candidate){
        return left.isSatisfiedBy(candidate) || right.isSatisfiedBy(candidate);
    }
}
