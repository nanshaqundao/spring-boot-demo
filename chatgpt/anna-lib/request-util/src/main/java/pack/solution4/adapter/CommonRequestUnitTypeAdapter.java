package pack.solution4.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import pack.solution4.model.CommonRequestUnit;

import java.io.IOException;
import java.util.Map;

public class CommonRequestUnitTypeAdapter extends TypeAdapter<CommonRequestUnit> {
    private final CommonRequestBodyUnitTypeAdapter requestBodyAdapter;

    public CommonRequestUnitTypeAdapter(CommonRequestBodyUnitTypeAdapter requestBodyAdapter) {
        this.requestBodyAdapter = requestBodyAdapter;
    }
    @Override
    public void write(JsonWriter out, CommonRequestUnit request) throws IOException {
        Gson gson = new Gson();
        out.beginObject();

        out.name("url").value(request.getUrl());
        out.name("httpMethod").value(request.getHttpMethod());

        if (request.getParametersMap() != null && !request.getParametersMap().isEmpty()) {
            out.name("parametersMap");
            gson.toJson(request.getParametersMap(), new TypeToken<Map<String, String>>(){}.getType(), out);
        }

        out.name("requestBody");

        requestBodyAdapter.write(out, request.getRequestBody());


        out.endObject();
    }

    @Override
    public CommonRequestUnit read(JsonReader in) throws IOException {
        // Implement deserialization if needed, otherwise leave it empty
        throw new UnsupportedOperationException("Not implemented");
    }
}
