package solution;

import model.EnvelopeMessage;

import java.util.Map;

public abstract class Product {
    protected EnvelopeMessage envelopeMessage;

    public Product(EnvelopeMessage envelopeMessage) {
        this.envelopeMessage = envelopeMessage;
    }

    public abstract String getRequestUrl();
    public abstract Map<String, String> getRequestHeaders();
    public abstract String getRequestBody();
}
