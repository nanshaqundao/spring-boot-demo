package solution;

import model.EnvelopeMessage;

import java.util.HashMap;
import java.util.Map;

public abstract class Product {
    protected EnvelopeMessage envelopeMessage;

    protected Map<String, ParameterValueProvider> valueProviders;

    public Product(EnvelopeMessage envelopeMessage) {
        this.envelopeMessage = envelopeMessage;
        valueProviders = new HashMap<>();
    }

    public abstract String getRequestURL();

    public abstract Map<String, String> getRequestHeaders();

    public abstract String getRequestBody();

    public String getParameterValue(String parameterName) {
        ParameterValueProvider provider = valueProviders.get(parameterName);
        return (provider != null) ? provider.getValue(envelopeMessage) : "";
    }
}