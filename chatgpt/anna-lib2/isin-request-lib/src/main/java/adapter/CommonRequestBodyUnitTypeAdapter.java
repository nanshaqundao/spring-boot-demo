package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.CommonRequestBodyUnit;
import model.FunctionalValue;

import java.io.IOException;
import java.util.Map;

public class CommonRequestBodyUnitTypeAdapter extends TypeAdapter<CommonRequestBodyUnit> {

    @Override
    public void write(JsonWriter out, CommonRequestBodyUnit requestBody) throws IOException {
        out.beginObject();

        if (requestBody.getStringPropertiesMap() != null) {
            for (Map.Entry<String, String> entry : requestBody.getStringPropertiesMap().entrySet()) {
                out.name(entry.getKey()).value(entry.getValue());
            }
        }

        if (requestBody.getFunctionPropertiesMap() != null) {
            for (Map.Entry<String, FunctionalValue> entry : requestBody.getFunctionPropertiesMap().entrySet()) {
                String key = entry.getKey();
                FunctionalValue functionalValue = entry.getValue();
                String value = functionalValue.getActualValue();
                out.name(key).value(value);
            }
        }

        out.endObject();
    }

    @Override
    public CommonRequestBodyUnit read(JsonReader in) throws IOException {
        // Implement deserialization if needed, otherwise leave it empty
        throw new UnsupportedOperationException("Not implemented");
    }
}
