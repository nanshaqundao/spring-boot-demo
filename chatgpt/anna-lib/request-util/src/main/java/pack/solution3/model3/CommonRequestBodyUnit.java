package pack.solution3.model3;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class CommonRequestBodyUnit {
    @Expose
    private Map<String, String> stringPropertiesMap = new HashMap<>();
    @Expose
    private Map<String, IReferencedValue> referencedPropertiesMap = new HashMap<>();

    public Map<String, String> getStringPropertiesMap() {
        return stringPropertiesMap;
    }

    public void setStringPropertiesMap(Map<String, String> stringPropertiesMap) {
        this.stringPropertiesMap = stringPropertiesMap;
    }

    public Map<String, IReferencedValue> getReferencedPropertiesMap() {
        return referencedPropertiesMap;
    }

    public void setReferencedPropertiesMap(Map<String, IReferencedValue> referencedPropertiesMap) {
        this.referencedPropertiesMap = referencedPropertiesMap;
    }
}
