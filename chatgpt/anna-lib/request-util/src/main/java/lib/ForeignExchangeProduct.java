package lib;

import model.EnvelopeMessage;

import java.util.Map;

public class ForeignExchangeProduct extends Product{
    public ForeignExchangeProduct(EnvelopeMessage envelopeMessage) {
        super(envelopeMessage);
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
