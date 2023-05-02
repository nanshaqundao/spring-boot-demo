package solution2;

import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Library {

    public Supplier<String> getReferenceValue(String reference, CalculateAPI api) {
        switch (reference) {
            case "outerValue":
                // Return a Supplier that will call the appropriate API to get the outerValue
                return () -> api.getOuterValue();
            case "crossRate":
                // Return a Supplier that will call the appropriate API to get the crossRate
                return () -> api.getCrossRate();
            default:
                return null;
        }
    }

    public Supplier<ApiRequestProvider> getReferenceAPIRequest(String reference, CalculateAPI api) {
        switch (reference) {
            case "outerValue":
                // Return a Supplier that will call the appropriate API to get the outerValue
                return () -> new ApiRequestProvider() {
                    @Override
                    public ApiRequest getApiRequest() {
                        return new ApiRequest("outerValue", "GET", "http://localhost:8080/outerValue");
                    }
                };
            case "crossRate":
                // Return a Supplier that will call the appropriate API to get the crossRate
                return () -> api.getCrossRate();
            default:
                return null;
        }
    }

    public void processRequestBodies(Map<String, ApiRequestBody> requestBodies, CalculateAPI api) {
        Pattern placeholderPattern = Pattern.compile("\\[(.*?)\\]");

        for (ApiRequestBody requestBody : requestBodies.values()) {
            Matcher matcher = placeholderPattern.matcher(requestBody.getValue());
            while (matcher.find()) {
                String placeholder = matcher.group(1);
                if (requestBodies.containsKey(placeholder)) {
                    ApiRequestBody referenceRequestBody = requestBodies.get(placeholder);
                    String referenceOutput = api.call(referenceRequestBody);
                    requestBody.setValue(requestBody.getValue().replace("[" + placeholder + "]", referenceOutput));
                } else {
                    Supplier<String> referenceValueSupplier = getReferenceValue(placeholder, api);
                    if (referenceValueSupplier != null) {
                        String referenceValue = referenceValueSupplier.get();
                        requestBody.setValue(requestBody.getValue().replace("[" + placeholder + "]", referenceValue));
                    }
                }
            }
        }
    }
}
