package pack.solution4.model;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class CommonRequestBodyUnit {
    @Expose
    private Map<String, String> stringPropertiesMap = new HashMap<>();
    @Expose
    private Map<String, FunctionalValue> functionPropertiesMap = new HashMap<>();

    public Map<String, String> getStringPropertiesMap() {
        return stringPropertiesMap;
    }

    public void setStringPropertiesMap(Map<String, String> stringPropertiesMap) {
        this.stringPropertiesMap = stringPropertiesMap;
    }

    public Map<String, FunctionalValue> getFunctionPropertiesMap() {
        return functionPropertiesMap;
    }

    public void setFunctionPropertiesMap(Map<String, FunctionalValue> functionPropertiesMap) {
        this.functionPropertiesMap = functionPropertiesMap;
    }
}
