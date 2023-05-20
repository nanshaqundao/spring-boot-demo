package pack.solution4.model;

import java.util.function.Function;

public class FunctionalValue {
    private String actualValue = "NOT INITIALISED";
    private boolean actualValueInitialised = false;
    private Function<Void, String> function;

    public FunctionalValue(Function<Void, String> function) {
        this.function = function;
    }

    public String getActualValue() {
        if (!actualValueInitialised) {
            actualValue = function.apply(null);
            actualValueInitialised = true;
        }
        return actualValue;
    }
}
