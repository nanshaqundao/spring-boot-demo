package lib;

import model.EnvelopeDocument;

import java.util.Map;

public class ForeignExchangeProduct extends Product{
    public ForeignExchangeProduct(EnvelopeDocument envelopeDocument) {
        super(envelopeDocument);
    }

    @Override
    public String getRequestURL() {
        return null;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    public String getRequestBody() {
        return null;
    }
}
