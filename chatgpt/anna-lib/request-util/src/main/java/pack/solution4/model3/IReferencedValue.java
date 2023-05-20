package pack.solution4.model3;

public interface IReferencedValue {
    String getStringValue();

    default boolean isReferencedValueReady() {
        return false;
    }
}
