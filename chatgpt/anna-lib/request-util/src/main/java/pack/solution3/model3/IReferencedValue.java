package pack.solution3.model3;

public interface IReferencedValue {
    String getStringValue();

    default boolean isReferencedValueReady() {
        return false;
    }
}
