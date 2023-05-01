package lib;

import model.EnvelopeDocument;

import java.util.Map;

public abstract class Product {
    protected EnvelopeDocument envelopeDocument;

    public Product(EnvelopeDocument envelopeDocument) {
        this.envelopeDocument = envelopeDocument;
    }

    public abstract String getRequestURL();
    public abstract Map<String, String> getRequestHeaders();
    public abstract String getRequestBody();
}
