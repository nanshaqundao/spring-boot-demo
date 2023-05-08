package pack.solution3.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import pack.solution3.model3.CommonRequestUnit;
import pack.solution3.model3.IReferencedValue;

import java.io.IOException;
import java.util.Map;

public class CommonRequestUnitTypeAdapter extends TypeAdapter<CommonRequestUnit> {

    @Override
    public void write(JsonWriter out, CommonRequestUnit request) throws IOException {
        out.beginObject();

        out.name("url").value(request.getUrl());
        out.name("httpMethod").value(request.getHttpMethod());

        out.name("requestBody");
        out.beginObject();
        for (Map.Entry<String, String> entry : request.getRequestBody().getStringPropertiesMap().entrySet()) {
            out.name(entry.getKey()).value(entry.getValue());
        }
        for (Map.Entry<String, IReferencedValue> entry : request.getRequestBody().getReferencedPropertiesMap().entrySet()) {
            out.name(entry.getKey()).value(entry.getValue().getStringValue());
        }
        out.endObject();

        out.endObject();
    }

    @Override
    public CommonRequestUnit read(JsonReader in) throws IOException {
        // Implement deserialization if needed, otherwise leave it empty
        throw new UnsupportedOperationException("Not implemented");
    }
}
