package pack.solution4.model;

public interface IReferencedValue {
    String getStringValue();

    default boolean isReferencedValueReady() {
        return false;
    }
}
