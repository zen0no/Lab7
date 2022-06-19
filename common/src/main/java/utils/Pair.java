package utils;

import java.util.Objects;

/**
 * Simple data structure
 * @param <K>
 * @param <V>
 */
public class Pair<K, V>{
    private K left;
    private V right;

    public Pair(K left, V right){
        this.left = left;
        this.right = right;
    }

    public void setLeft(K left) {
        this.left = left;
    }

    public void setRight(V right) {
        this.right = right;
    }

    public K getLeft() {
        return left;
    }

    public V getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
